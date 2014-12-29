package com.smartgwt.mobile.client.widgets.grid;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.smartgwt.mobile.SGWTInternal;
import com.smartgwt.mobile.client.data.Record;

public class GroupNode {

    @SGWTInternal
    public static final Comparator<GroupNode> _COMPARATOR = new Comparator<GroupNode>() {

        @Override
        @SuppressWarnings("unchecked")
        public int compare(GroupNode leftGN, GroupNode rightGN) {
            if (leftGN.groupValue == null) {
                return rightGN.groupValue == null ? 0 : -1;
            } else if (rightGN.groupValue == null) {
                assert leftGN.groupValue != null;
                return 1;
            } else {
                assert leftGN.groupValue != null && rightGN.groupValue != null;
                if (leftGN.groupValue instanceof Comparable &&
                        rightGN.groupValue instanceof Comparable)
                {
                    try {
                        return ((Comparable<Object>)leftGN.groupValue).compareTo(rightGN.groupValue);
                    } catch (ClassCastException ex) {}
                }
                return leftGN.groupValueString.compareTo(rightGN.groupValueString);
            }
        }
    };

    private Object groupValue;
    private Record[] groupMembers;
    private String groupTitle;

    private String groupValueString;
    private List<Record> groupMembersList;

    public GroupNode(Object groupValue) {
        this.groupValue = groupValue;
        this.groupValueString = groupValue == null ? null : groupValue.toString();
    }

    public final Object getGroupValue() {
        return groupValue;
    }

    @SGWTInternal
    public final String _getGroupValueString() {
        return groupValueString;
    }

    @SGWTInternal
    public List<Record> _getGroupMembersList() {
        return groupMembersList;
    }

    @SGWTInternal
    public void _add(Record member) {
        if (groupMembersList == null) groupMembersList = new ArrayList<Record>();
        groupMembersList.add(member);
    }

    public final Record[] getGroupMembers() {
        if (groupMembersList != null &&
            (groupMembers == null ||
             groupMembers.length != groupMembersList.size()))
        {
            groupMembers = groupMembersList.toArray(new Record[groupMembersList.size()]);
        } else groupMembers = new Record[0];
        return groupMembers;
    }

    public final String getGroupTitle() {
        return groupTitle;
    }

    @SGWTInternal
    public void _setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }
}
