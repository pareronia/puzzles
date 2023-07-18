package org.collegeboard.apcentral.ap23_frq_comp_sci_a;

import java.util.Objects;

public class BoxOfCandy {
    
    private final Candy[][] box;
    
    public BoxOfCandy(final Candy[][] box) {
        this.box = box;
    }
    
    public Candy[][] getBox() {
        return box;
    }
    
    public boolean moveCandyToFirstRow(final int col) {
        if (box[0][col] != null) {
            return true;
        }
        for (int row = 1; row < box.length; row++) {
            if (box[row][col] != null) {
                box[0][col] = box[row][col];
                box[row][col] = null;
                return true;
            }
        }
        return false;
    }
    
    public Candy removeNextByFlavor(final String flavor) {
        for (int row = box.length - 1; row >= 0; row--) {
            for (int col = 0; col < box[row].length; col++) {
                final Candy candy = box[row][col];
                if (candy != null && candy.getFlavor().equals(flavor)) {
                    box[row][col] = null;
                    return candy;
                }
            }
        }
        return null;
    }

    public static class Candy {
        final String flavor;

        public Candy(final String flavor) {
            this.flavor = flavor;
        }

        public String getFlavor() {
            return flavor;
        }

        @Override
        public int hashCode() {
            return Objects.hash(flavor);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Candy other = (Candy) obj;
            return Objects.equals(flavor, other.flavor);
        }
    }
}
