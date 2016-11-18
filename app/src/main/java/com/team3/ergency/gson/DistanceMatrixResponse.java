package com.team3.ergency.gson;

import java.util.List;

/**
 * Created by howard on 11/14/16.
 */


public class DistanceMatrixResponse {

    /**
     * destination_addresses : ["Kaiser Permanente Orange County - Irvine Medical Center, 6640 Alton Pkwy, Irvine, CA 92618, USA","Hoag Hospital Irvine, 16200 Sand Canyon Ave, Irvine, CA 92618, USA","Hoag Urgent Care Newport Beach, 500 Superior Ave #160, Newport Beach, CA 92663, USA","Hoag Hospital Emergency, 1 Hoag Dr, Newport Beach, CA 92663, USA","Kaiser Permanente Harbor-MacArthur Medical Offices, 3401 Harbor Blvd, Santa Ana, CA 92704, USA","Hoag Urgent Care Tustin, 2560 Bryan Ave A, Tustin, CA 92780, USA","Huntington Beach Urgent Care, 17822 Beach Blvd #100, Huntington Beach, CA 92647, USA","Choc Health Alliance, 1120 W La Veta Ave # 450, Orange, CA 92868, USA","CHOC Children's, 1201 W La Veta Ave, Orange, CA 92868, USA","Kaiser Permanente Garden Grove Medical Offices, 12100 Euclid St, Garden Grove, CA 92840, USA"]
     * origin_addresses : ["8 Curie Ct, Irvine, CA 92617, USA"]
     * rows : [{"elements":[{"distance":{"text":"9.0 km","value":9001},"duration":{"text":"14 mins","value":816},"status":"OK"},{"distance":{"text":"9.0 km","value":9046},"duration":{"text":"14 mins","value":825},"status":"OK"},{"distance":{"text":"15.0 km","value":15005},"duration":{"text":"16 mins","value":937},"status":"OK"},{"distance":{"text":"14.5 km","value":14546},"duration":{"text":"15 mins","value":919},"status":"OK"},{"distance":{"text":"12.2 km","value":12221},"duration":{"text":"12 mins","value":700},"status":"OK"},{"distance":{"text":"20.4 km","value":20424},"duration":{"text":"17 mins","value":1011},"status":"OK"},{"distance":{"text":"19.2 km","value":19210},"duration":{"text":"19 mins","value":1116},"status":"OK"},{"distance":{"text":"23.6 km","value":23557},"duration":{"text":"17 mins","value":1044},"status":"OK"},{"distance":{"text":"22.1 km","value":22099},"duration":{"text":"19 mins","value":1134},"status":"OK"},{"distance":{"text":"30.7 km","value":30743},"duration":{"text":"25 mins","value":1493},"status":"OK"}]}]
     * status : OK
     */

    private List<RowsBean> rows;

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        private List<ElementsBean> elements;

        public List<ElementsBean> getElements() {
            return elements;
        }

        public void setElements(List<ElementsBean> elements) {
            this.elements = elements;
        }

        public static class ElementsBean {
            /**
             * distance : {"text":"9.0 km","value":9001}
             * duration : {"text":"14 mins","value":816}
             * status : OK
             */

            private DistanceBean distance;
            private DurationBean duration;
            private String status;

            public DistanceBean getDistance() {
                return distance;
            }

            public void setDistance(DistanceBean distance) {
                this.distance = distance;
            }

            public DurationBean getDuration() {
                return duration;
            }

            public void setDuration(DurationBean duration) {
                this.duration = duration;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public static class DistanceBean {
                /**
                 * text : 9.0 km
                 * value : 9001
                 */

                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }

            public static class DurationBean {
                /**
                 * text : 14 mins
                 * value : 816
                 */

                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }
        }
    }
}
