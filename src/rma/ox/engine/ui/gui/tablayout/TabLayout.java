package rma.ox.engine.ui.gui.tablayout;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Array;

import rma.ox.engine.utils.Logx;

public class TabLayout extends Stack implements TabGroup.TabListener {

    private Array<Container> listLayout;
    private int index = 0;

    public TabLayout() {
        listLayout = new Array<>();
    }

    public TabLayout(TabGroup tabGroup) {
        tabGroup.addTabLayoutListener(this);
        listLayout = new Array<>();
    }

    public void addLayout(WidgetGroup widgetGroup) {
        Container container = new Container(widgetGroup).fill();
        listLayout.add(container);
        add(container);
    }

    @Override
    public boolean onSelected(int index) {
        Logx.l("onSelected : " + index + " size : " + listLayout.size);
        if (index > listLayout.size - 1) return false;
        for (int i = 0; i < listLayout.size; i++) {
            listLayout.get(i).setVisible(false);
        }
        listLayout.get(index).setVisible(true);
        this.index = index;
        return true;
    }

    public int getSize() {
        return listLayout.size;
    }

    public int getIndex() {
        return index;
    }
}
