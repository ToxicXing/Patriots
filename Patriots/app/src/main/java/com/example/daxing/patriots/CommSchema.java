package com.example.daxing.patriots;

import java.util.ArrayList;

/**
 * Created by DaXing on 16/11/22.
 */

public class CommSchema {
    public ArrayList<CommSchema.Committee> results;
    public class Committee {
        public String name;
        public String chamber;
        public String committee_id;
        public String subcommittee;
        public String parent_committee_id;
        public String phone;
        public String office;
    }
}
