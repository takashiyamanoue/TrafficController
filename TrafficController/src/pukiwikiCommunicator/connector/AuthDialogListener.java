package pukiwikiCommunicator.connector;

public interface AuthDialogListener {
    public void whenLoginButtonClicked(AuthDialog x);
    public void whenCancelButtonClicked(AuthDialog x);
}
