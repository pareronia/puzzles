package org.collegeboard.apcentral.ap23_frq_comp_sci_a;

import static org.assertj.core.api.Assertions.assertThat;

import org.collegeboard.apcentral.ap23_frq_comp_sci_a.BoxOfCandy.Candy;
import org.junit.jupiter.api.Test;

class BoxOfCandyTest {

    @Test
    void moveCandyToFirstRow() {
        final Candy[][] theBox = new Candy[][] {
            new Candy[] { null, new Candy("lime"),      null },
            new Candy[] { null, new Candy("orange"),    null },
            new Candy[] { null, null,                   new Candy("cherry") },
            new Candy[] { null, new Candy("lemon"),     new Candy("grape") },
        };
        final BoxOfCandy box = new BoxOfCandy(theBox);
        
        assertThat(box.moveCandyToFirstRow(0)).isFalse();
        assertThat(box.getBox()).isDeepEqualTo(theBox);
        assertThat(box.moveCandyToFirstRow(1)).isTrue();
        assertThat(box.getBox()).isDeepEqualTo(theBox);
        assertThat(box.moveCandyToFirstRow(2)).isTrue();
        assertThat(box.getBox()).isDeepEqualTo(new Candy[][] {
            new Candy[] { null, candy("lime"),      candy("cherry") },
            new Candy[] { null, candy("orange"),    null },
            new Candy[] { null, null,               null },
            new Candy[] { null, candy("lemon"),     candy("grape") }});
    }
    
    @Test
    void removeNextByFlavor() {
        final BoxOfCandy box = new BoxOfCandy(new Candy[][] {
            new Candy[] { candy("lime"), candy("lime"), null, candy("lemon"), null },
            new Candy[] { candy("orange"), null, null, candy("lime"), candy("lime") },
            new Candy[] { candy("cherry"), null, candy("lemon"), null, candy("orange") }
        });
        
        assertThat(box.removeNextByFlavor("cherry")).isNotNull()
                .extracting(Candy::getFlavor).isEqualTo("cherry");
        assertThat(box.removeNextByFlavor("lime")).isNotNull()
                .extracting(Candy::getFlavor).isEqualTo("lime");
        assertThat(box.removeNextByFlavor("grape")).isNull();
        assertThat(box.getBox()).isDeepEqualTo(new Candy[][] {
            new Candy[] { candy("lime"), candy("lime"), null, candy("lemon"), null },
            new Candy[] { candy("orange"), null, null, null, candy("lime") },
            new Candy[] { null, null, candy("lemon"), null, candy("orange") }});
    }
    
    private Candy candy(final String flavor) {
        return new Candy(flavor);
    }
}
