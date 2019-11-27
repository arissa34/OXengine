package rma.ox.engine.ui.gui.notification;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.MyTable;

import rma.ox.engine.ui.gui.scroll.MyScrollPane;


public class InAppNotificationTable {

    public static final float PAD = 10f;
    private static final float WIDTH = 300;

    private Container container;
    private MyScrollPane scrollPane;
    private MyTable table;

    public InAppNotificationTable(){
        scrollPane = new MyScrollPane(table = new MyTable());
        scrollPane.setSmoothScrolling(true);
        table.defaults().width(WIDTH).right();
    }

    public void addNotif(InAppNotification notification){
        table.add(notification).row();
        scrollPane.scrollTo(0, table.getPrefHeight(), 0, 0);
        notification.setPosition(table.getPrefWidth(), table.getPrefHeight()- notification.getPrefHeight());
        MoveToAction action = Actions.action(MoveToAction.class);
        action.setPosition(0, table.getPrefHeight()- notification.getPrefHeight());
        action.setDuration(0.25f);
        notification.addAction(action);
        notification.invalidate();
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
