package org.collegeboard.apcentral.ap23_frq_comp_sci_a;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SignTest {

	@Test
	void test1() {
		final Sign sign1 = new Sign("ABC222DE", 3);
		
		assertThat(sign1.numberOfLines()).isEqualTo(3);
		assertThat(sign1.getLines()).isEqualTo("ABC;222;DE");
	}
	
	@Test
	void test2() {
		final Sign sign2 = new Sign("ABCD", 10);
		
		assertThat(sign2.numberOfLines()).isEqualTo(1);
		assertThat(sign2.getLines()).isEqualTo("ABCD");
	}

	@Test
	void test3() {
		final Sign sign3 = new Sign("ABCDEF", 6);
		
		assertThat(sign3.numberOfLines()).isEqualTo(1);
		assertThat(sign3.getLines()).isEqualTo("ABCDEF");
	}

	@Test
	void test4() {
		final Sign sign4 = new Sign("", 4);
		
		assertThat(sign4.numberOfLines()).isEqualTo(0);
		assertThat(sign4.getLines()).isEqualTo("");
	}

	@Test
	void test5() {
		final Sign sign5 = new Sign("AB_CD_EF", 2);
		
		assertThat(sign5.numberOfLines()).isEqualTo(4);
		assertThat(sign5.getLines()).isEqualTo("AB;_C;D_;EF");
	}
}
