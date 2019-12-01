package rma.ox.engine.ui.gui.notification;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.MyTable;
import com.badlogic.gdx.utils.Align;
import rma.ox.engine.ui.gui.scroll.MyScrollPane;

public class InAppNotificationTable {

    protected static final float PAD = 10f;
    protected static final float WIDTH = 300;
    protected static final float HEADER_HEIGHT = 180;

    private Container container;
    private MyScrollPane scrollPane;
    private MyTable table;

    public InAppNotificationTable(){
        scrollPane = new MyScrollPane(table = new MyTable());
        scrollPane.setSmoothScrolling(true);
        table.defaults().width(WIDTH);
    }

    public void addNotif(InAppNotification notification){
        table.add(notification).row();
        table.pack(); // Needed here to fix a visual bug
        scrollPane.scrollTo(0, table.getPrefHeight(), 0, 0);
        MoveToAction action = Actions.action(MoveToAction.class);
        if(Align.isRight(container.getAlign())){
            notification.setPosition(table.getPrefWidth(), table.getPrefHeight()- notification.getPrefHeight());
            notification.invalidate();
            action.setPosition(0, table.getPrefHeight()- notification.getPrefHeight());
        }else {
            notification.setPosition(-table.getPrefWidth(), table.getPrefHeight()- notification.getPrefHeight());
            notification.invalidate();
            action.setPosition(0, table.getPrefHeight()- notification.getPrefHeight());
        }
        action.setDuration(0.25f);
        notification.addAction(action);
    }

    public void removeNotif(InAppNotification notification){
        table.removeActor(notification);
    }

    public Container getActor() {
        if(container == null){
            container = new Container<>(scrollPane);
        }
        return container;
    }
}
