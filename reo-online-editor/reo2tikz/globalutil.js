String.prototype.format = function () {
  let i = 0, args = arguments;
  return this.replace(/@@/g, function () {
    return args[i] !== undefined ? args[i++] : '';
  })
};

function solveExpression(expr, env) {
  let execstr = "";
  for (let sym in env)
    execstr += "let @@=@@;".format(sym, env[sym]);
  return eval(execstr + expr);
}

function parseNumber(expr, env) {
  if (typeof expr === 'number')
    return expr;
  return solveExpression(expr, env);
}

function parseNumberArray(arr, env) {
  for (let i = 0; i < arr.length; i++)
    arr[i] = parseNumber(arr[i], env)
  return arr;
}