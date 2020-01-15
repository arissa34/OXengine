package rma.ox.engine.ui.gui;

public interface ILayoutLoader {
    void startLoading();
    void finishLoadingSuccess();
    boolean isLoading();
}
