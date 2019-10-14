operation I_op (target : Qubit) : Int {
    body {
        I(target);
        return 1;
    }
}

operation X_op (target : Qubit) : Int {
    body {
        X(target);
        return 1;
    }
}

operation Z_op (target : Qubit) : Int {
    body {
        Z(target);
        return 1;
    }
}
