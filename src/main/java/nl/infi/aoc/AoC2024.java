package nl.infi.aoc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <h1><a href="https://aoc.infi.nl/2024">Sneeuwvlokken en sneeuwblokken</a></h1>
 */
public class AoC2024 extends AocBase {

    private static final int N = 30;

    private final XM4SMachine xm4sMachine;

    protected AoC2024(final boolean debug, final List<String> input) {
        super(debug);
        this.xm4sMachine = new XM4SMachine(input);
    }

    public static AoC2024 createDebug(final List<String> input) {
        return new AoC2024(true, input);
    }

    public static AoC2024 create(final List<String> input) {
        return new AoC2024(false, input);
    }

    private Luchtruim lucht() {
        final Luchtruim lucht = new Luchtruim();
        for (final Blokje blokje : alleBlokjes()) {
            lucht.put(blokje,
                    this.xm4sMachine.programmaUitvoeren(blokje));
        }
        return lucht;
    }

    @Override
    public Integer solvePart1() {
        return lucht().values().sum();
    }

    @Override
    public Integer solvePart2() {
        class BFS {
            final Luchtruim lucht;

            public BFS(final Luchtruim lucht) {
                this.lucht = lucht;
            }

            public Luchtruim bfs(final Blokje start) {
                final var wolk = new Luchtruim();
                final var q = new ArrayDeque<>(List.of(start));
                while (!q.isEmpty()) {
                    q.poll().buren()
                        .filter(Blokje::binnen)
                        .filter(lucht::isWolk)
                        .filter(buur -> !wolk.isWolk(buur))
                        .forEach(buur -> {
                            q.add(buur);
                            wolk.add(buur);
                        });
                }
                return wolk;
            }
        }

        final Luchtruim lucht = lucht();
        final var bfs = new BFS(lucht);
        int ans = 0;
        final var gezien = new Luchtruim();
        for (final Blokje blokje : alleBlokjes()) {
            if (gezien.isWolk(blokje)) {
                continue;
            }
            if (lucht.isWolk(blokje)) {
                gezien.addAll(bfs.bfs(blokje));
                ans += 1;
            } else {
                gezien.add(blokje);
            }
        }
        return ans;
    }

    private static final class XM4SMachine {
        enum Op { PUSH_X, PUSH_Y, PUSH_Z, PUSH, ADD, JMPOS, RET }

        record Instructie(Op op, String operand) {
            private static Instructie fromString(final String s) {
                final String[] sp = s.split(" ");
                return switch (sp[0]) {
                    case "push" -> {
                        yield switch (sp[1]) {
                            case "X" -> new Instructie(Op.PUSH_X, null);
                            case "Y" -> new Instructie(Op.PUSH_Y, null);
                            case "Z" -> new Instructie(Op.PUSH_Z, null);
                            default -> new Instructie(Op.PUSH, sp[1]);
                        };
                    }
                    case "add" -> new Instructie(Op.ADD, null);
                    case "jmpos" -> new Instructie(Op.JMPOS, sp[1]);
                    case "ret" -> new Instructie(Op.RET, null);
                    default -> throw new IllegalArgumentException("Unexpected");
                };
            }
        }

        private final List<Instructie> instructies;

        public XM4SMachine(final List<String> instructies) {
            this.instructies = instructies.stream()
                    .map(Instructie::fromString)
                    .toList();
        }

        public int programmaUitvoeren(final Blokje blokje) {
            final var stack = new ArrayDeque<Integer>();
            int ip = 0;
            while (true) {
                final Instructie ins = instructies.get(ip);
                switch (ins.op) {
                    case PUSH_X -> stack.addFirst(blokje.x);
                    case PUSH_Y -> stack.addFirst(blokje.y);
                    case PUSH_Z -> stack.addFirst(blokje.z);
                    case PUSH -> stack.addFirst(Integer.valueOf(ins.operand));
                    case ADD -> stack.addFirst(
                                    stack.pollFirst() + stack.pollFirst());
                    case JMPOS -> {
                        if (stack.pollFirst() >= 0) {
                            ip += Integer.parseInt(ins.operand);
                        }
                    }
                    case RET -> { return stack.pollFirst(); }
                }
                ip++;
            }
        }
    }

    record Blokje(int x, int y, int z) {
        private static final Set<int[]> DIRS = Set.of(
            new int[] {1, 0, 0}, new int[] {-1, 0, 0},
            new int[] {0, 1, 0}, new int[] {0, -1, 0},
            new int[] {0, 0, 1}, new int[] {0, 0, -1}
        );

        public boolean binnen() {
            return 0 <= x && x < N && 0 <= y && y < N && 0 <= z && z < N;
        }

        public Stream<Blokje> buren() {
            return DIRS.stream()
                    .map(d -> new Blokje(x + d[0], y + d[1], z + d[2]));
        }
    }

    private static final class Luchtruim {
        private final int[] opslag = new int[N * N * N];

        private int index(final Blokje blokje) {
            return blokje.x * N * N + blokje.y * N + blokje.z;
        }

        public boolean isWolk(final Blokje blokje) {
            return opslag[index(blokje)] != 0;
        }

        public void put(final Blokje blokje, final int value) {
            opslag[index(blokje)] = value;
        }

        public void add(final Blokje blokje) {
            opslag[index(blokje)] = 1;
        }

        public void addAll(final Luchtruim luchtRuim) {
            IntStream.range(0, N * N * N).forEach(index -> {
                if (luchtRuim.opslag[index] == 1) {
                    this.opslag[index] = 1;
                }
            });
        }

        public IntStream values() {
            return Arrays.stream(opslag);
        }
    }

    private Iterable<Blokje> alleBlokjes() {
        return () -> new Iterator<>() {
            final int i_max = N * N * N;
            int i = 0;
            
            @Override
            public Blokje next() {
                final int z = i % N, y = (i / N) % N, x = (i / N / N);
                i++;
                return new Blokje(x, y, z);
            }
            
            @Override
            public boolean hasNext() {
                return i < i_max;
            }
        };
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2024.createDebug(TEST).solvePart1() == 5686200;

        final var path = Paths.get("src/main/resources/AoC2024.txt");
		final var lines = Files.readAllLines(path);
		lap("Part 1", () -> AoC2024.create(lines).solvePart1()); // 5255
		lap("Part 2", () -> AoC2024.create(lines).solvePart2()); // 19
    }
    
    private static final List<String> TEST = """
            push 999
            push X
            push -3
            add
            jmpos 2
            ret
            ret
            push 123
            ret
            """.lines().toList();
}
