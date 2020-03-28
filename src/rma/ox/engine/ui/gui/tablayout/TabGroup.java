package rma.ox.engine.ui.gui.tablayout;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.GdxRuntimeException;

import rma.ox.engine.ui.gui.button.CustomCheckBox;
import rma.ox.engine.ui.gui.utils.LabelUtils;

public class TabGroup extends ScrollPane {

    public interface TabListener{
        boolean onSelected(int index);
    }

    private Table table;
    private Array<Cell> listTabsCell;
    private ArrayMap<String, CustomCheckBox> listTabsKey;
    private int indexSelected = -1;
    private TabListener tabLayoutListener;
    private TabListener listener;

    public TabGroup(){
        super(new Table());
        table = (Table) getActor();
        listTabsCell = new Array<>();
        listTabsKey = new ArrayMap();
        setScrollingDisabled(false, true);
    }

    public TabGroup addListener(TabListener listener){
        this.listener = listener;
        return this;
    }

    protected TabGroup addTabLayoutListener(TabListener listener){
        this.tabLayoutListener = listener;
        return this;
    }

    public Table getTable() {
        return table;
    }

    public void addTabs(TextureAtlas atlas, String pathOn, String pathOff, String pathDisable, BitmapFont font, boolean isNinePatch, String ...txt){
        for(int i = 0; i < txt.length; i++){
            addTab(atlas, pathOn, pathOff, pathDisable, font, isNinePatch, txt[i]);
        }
        table.getCells().get(0).padLeft(10);
        table.getCells().get(table.getCells().size-1).padRight(20);
    }

    public void addTab(TextureAtlas atlas, String pathOn, String pathOff, String pathDisable, BitmapFont font, boolean isNinePatch, String txt){
        final int index =  listTabsKey.size;
        final CustomCheckBox customCheckBox = new CustomCheckBox(
                atlas,
                pathOn,
                pathOff,
                pathDisable,
                font,
                isNinePatch
        );
        customCheckBox.setText(txt);
        customCheckBox.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(index == indexSelected){
                    customCheckBox.setChecked(true);
                    return;
                }
                if(customCheckBox.isChecked()){
                    select(index);

                    if(listener != null){
                        listener.onSelected(indexSelected);
                    }
                }
            }
        });
        listTabsKey.put(txt, customCheckBox);
        //listTabs.add(customCheckBox);
        Cell cell = table.add(customCheckBox.getActor());
        listTabsCell.add(cell);
        cell.padRight(-16);
    }

    public Cell getTabCell(String key){
        if(!listTabsKey.containsKey(key)) throw new GdxRuntimeException("Unkonw tab key");
        int index = listTabsKey.indexOfKey(key);
        return listTabsCell.get(index);
    }

    public void hideTab(String key){
        if(!listTabsKey.containsKey(key)) throw new GdxRuntimeException("Unkonw tab key");
        int index = listTabsKey.indexOfKey(key);
        listTabsCell.get(index).width(0).pad(0).getActor().setVisible(false);
    }

    public void showTab(String key){
        if(!listTabsKey.containsKey(key)) throw new GdxRuntimeException("Unkonw tab key");
        int index = listTabsKey.indexOfKey(key);
        listTabsCell.get(index).setActor(listTabsKey.get(key).getActor())
                .width(LabelUtils.getTexttWidth(listTabsKey.get(key).getActor().getLabel()))
                .padLeft(36).padRight(20);
        listTabsKey.get(key).getActor().setVisible(true);
    }

    private void select(int index){
        for(int i =0; i < listTabsKey.size; i++){
            listTabsKey.getValueAt(i).setChecked(false);
        }
        if(index > listTabsKey.size-1) return;
        listTabsKey.getValueAt(index).setChecked(true);
        indexSelected = index;
        if(tabLayoutListener != null){
            tabLayoutListener.onSelected(indexSelected);
        }
    }

    public int getIndexSelected(){
        return indexSelected;
    }

    @Override
    public void layout() {
        super.layout();
        if(listTabsKey.size > 0){
            select(0);
        }
    }
}
