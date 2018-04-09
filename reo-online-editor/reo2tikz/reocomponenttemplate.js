if (typeof module !== 'undefined') {
	ReoComponent = require('./reocomponent.js');
	ReoComponentImplementation = require('./reocomponentimplementation.js');
}

function ReoComponentTemplate(network, cName, argsTempl, argsIn, argsOut, innerScopeStr, metadata) {
	this.network = network;
	this.cName = cName;
	this.innerScopeStr = innerScopeStr;
	this.mdata = metadata;
	this.argsTempl = argsTempl;
	this.argsIn = argsIn;
	this.argsOut = argsOut;
	this.implementationCount = 0;
}

ReoComponentTemplate.prototype.implement = async function(binding) {
	let implName = this.cName + 'I'.repeat(this.implementationCount);
	this.implementationCount++;
	let component = new ReoComponentImplementation(this.network, this.cName, implName, this.argsIn, this.argsOut);

	if (binding.length != this.argsTempl.length) {
		throw 'incorrect amount of template args';
	}

	let env = {};
	for (let i = 0; i < binding.length; i++) {
		env[this.argsTempl[i]] = binding[i];
	}

	// IO set
	for (let rawIn of this.argsIn) { component.isIO[component.genNodeName(rawIn, env)] = true; }
	for (let rawOut of this.argsOut) { component.isIO[component.genNodeName(rawOut, env)] = true; }
	
	let metaDeep = JSON.parse(JSON.stringify(this.mdata));
	for (let s of metaDeep) {
		await component.procMeta(s, env);
	}

	await component.parseInnerStr(this.innerScopeStr, env); // todo binding

	component.inferMissingMeta();
	if (this.network.cfgNormalize) {
		component.normalizePositions();
	} else {
		// just flip Y
		component.scale(1, -1);
	}

	if (!component.bound) {
		component.inferBound();
	}
	component.shift([(component.bound[0][0] - component.bound[1][0]) * 0.5 - component.bound[0][0], (component.bound[0][1] - component.bound[1][1]) * 0.5 - component.bound[0][1]]);

	for (let comp of component.innerComponents) {
		component.dependencies[comp.typeName] = true;
	}

	//this.componentImplementations[m[1]] = component;

	// generate on the fly
	let network = this.network;
	let argsIn = this.argsIn;
	let argsOut = this.argsOut;
	let cName = implName;

	function ReoComponentComposition() {

	}
	ReoComponentComposition.prototype = Object.create(ReoComponent.prototype);
	ReoComponentComposition.prototype.visualName = this.cName;
	ReoComponentComposition.prototype.typeName = implName;
	ReoComponentComposition.prototype.define = function(definestate, outp) {
		component.define(definestate, outp);
		let argList = '';
		for (let i = 0; i < argsIn.length + argsOut.length; i++) {
			argList += ',#' + (i+3);
		}
		outp.value += '\\def \\reodraw@@ !#1,#2@@!{\n'.format(this.typeName, argList);
		outp.value += '  \\begin{scope}[shift=(#1),rotate around={#2:(#1)}]\n';
		outp.value += '    \\draw[line width=1pt, draw=gray] (@@,@@) rectangle (@@,@@);\n'.format(component.bound[0][0], component.bound[0][1], component.bound[1][0], component.bound[1][1]);
		outp.value += '    \\node[rotate=#2] at (0, @@) {\\textsc{@@}};\n'.format(Math.max(component.bound[0][1], component.bound[1][1]) + 0.15, this.visualName);
		outp.value += '    \\reoimpldraw@@!!\n'.format(this.typeName); // can draw a text alternatively
		for (let i = 0; i < argsIn.length; i++) {
			outp.value += '    \\path[draw,decoration={markings, mark=at position 0.0 with \\arrowstylerev},postaction=decorate] (@@) to #@@;\n'.format(component.genNodeName(argsIn[i], env), i+3);
		}
		for (let i = 0; i < argsOut.length; i++) {
			outp.value += '    \\path[draw,decoration={markings, mark=at position 1.0 with \\arrowstyle},postaction=decorate] (@@) to #@@;\n'.format(component.genNodeName(argsOut[i], env), i+argsIn.length+3);
		}
		outp.value += '  \\end{scope}\n';
		outp.value += '}\n';
	}
	ReoComponentComposition.prototype.draw = function(outp) {
		let argList = '';
		for (let i = 0; i < argsIn.length + argsOut.length; i++) {
			argList += ', ' + this.genPath(this.waypointsToPortIndex[i]);
		}
		outp.value += ('  \\coordinate (tmp) at ($(@@,@@)$);\n'.format(this.pos[0], this.pos[1]));
		outp.value += ('  \\reodraw@@!tmp, @@@@!;\n'.format(this.typeName, this.angle, argList));
	}


	//this.componentDefinitions[m[1]] = ReoComponentComposition;

	return ReoComponentComposition;
}

if (typeof module !== 'undefined') {
	module.exports = ReoComponentTemplate;
}