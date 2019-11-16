package rma.ox.engine.ui.gui.tablayout;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Array;

public class TabLayout{/* extends Stack implements TabGroup.TabListener{

    private TabGroup tabGroup;
    private Array<Container> listLayout;

    public TabLayout(TabGroup tabGroup){
        this.tabGroup = tabGroup;
        this.tabGroup.addTabLayoutListener(this);
        listLayout = new Array<>();
    }

    public void addLayout(WidgetGroup widgetGroup){
        Container container = new Container(widgetGroup).fill();
        listLayout.add(container);
        add(container);
    }

    @Override
    public void onSelected(int index) {
        if(index > listLayout.size-1) return;
        for(int i = 0; i < listLayout.size; i++){
            listLayout.get(i).setVisible(false);
        }
        listLayout.get(index).setVisible(true);
    }*/
}
