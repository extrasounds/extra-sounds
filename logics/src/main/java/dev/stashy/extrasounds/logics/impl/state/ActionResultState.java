package dev.stashy.extrasounds.logics.impl.state;

public enum ActionResultState {
    SUCCESS,
    CONSUME,
    PASS,
    FAIL;

    public static boolean isSuccess(ActionResultState actionResult) {
        return actionResult == SUCCESS || actionResult == CONSUME;
    }
}
