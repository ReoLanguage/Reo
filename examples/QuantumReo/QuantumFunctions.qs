operation I_op (target : Qubit) : Qubit {
    body {
        I(target);
        return target;
    }
}

operation X_op (target : Qubit) : Qubit {
    body {
        X(target);
        return target;
    }
}

operation Z_op (target : Qubit) : Qubit {
    body {
        Z(target);
        return target;
    }
}
