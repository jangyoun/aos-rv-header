package com.leejangyoun.recyclerviewwithheader;

import java.util.ArrayList;
import java.util.List;

public class Fruits {

    // =======================================================================
    // METHOD : enum
    // =======================================================================
    public enum TYPE {
        ITEM {
            @Override
            public String toString() {return "ITEM";}
        },
        GROUP {
            @Override
            public String toString() {return "GROUP";}
        },
        SLIDE {
            @Override
            public String toString() {return "SLIDE";}
        },
        PROGRESS {
            @Override
            public String toString() {return "PROGRESS";}
        }
    }

    // =======================================================================
    // METHOD : CustomItem
    // =======================================================================
    private TYPE type;
    private Fruit item;
    private int groupNo;
    private String groupTitle;
    private List<Fruit> groupArr;

    public Fruits(TYPE itemType) {
        this.type = itemType;
    }

    // universal
    public TYPE getType() {
        return type;
    }


    // fruit item
    public void setFruitItem(Fruit item) {
        this.item = item;
    }
    public Fruit getItem() {
        return item;
    }


    // group
    public void setGroups(int groupNo, String groupTitle) {
        this.groupNo = groupNo;
        this.groupTitle = groupTitle;
        groupArr = new ArrayList<>();
    }
    public void addGroups(Fruit item) {
        groupArr.add(item);
    }
    public List<Fruit> getGroupArr() {
        return groupArr;
    }
    public String getGroupTitle() {
        return groupTitle;
    }

    // =======================================================================
    // METHOD : MagazineItm
    // =======================================================================
    public class Fruit {

        private int no;
        private String title;
        private String thumb;

        public Fruit(int no, String title, String thumb) {
            this.no    = no;
            this.title = title;
            this.thumb = thumb;
        }

        public int getNo() {
            return no;
        }

        public String getTitle() {
            return title;
        }

        public String getThumb() {
            return thumb;
        }
    }
}
