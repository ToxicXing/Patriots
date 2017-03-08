package com.example.daxing.patriots;

import java.util.ArrayList;

/**
 * Created by DaXing on 16/11/18.
 */

public class BillSchema {
    public ArrayList<BillSchema.Bill> results;
    public class Bill {
        public String bill_id;
        public String bill_type;
        public String chamber;
        public String short_title;
        public String official_title;
        public Sponsor sponsor;
        public String introduced_on;
        public Urls urls;
        public LastVersion last_version;
    }
    public class Sponsor {
        public String first_name;
        public String last_name;
        public String title;
        public Urls urls;
    }

    public class Urls {
        public String congress;
    }

    public class LastVersion {
        public String version_name;
        public PdfUrls urls;
    }

    public class PdfUrls {
        public String pdf;
    }

}
