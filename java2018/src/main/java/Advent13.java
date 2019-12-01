import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;


@SuppressWarnings("ProhibitedExceptionDeclared")
public final class Advent13 {

    private static final int HORIZONTAL      = '-';
    private static final int VERTICAL        = '|';
    private static final int CURVE_SLASH     = '/';
    private static final int CURVE_BACKSLASH = '\\';
    private static final int INTERSECTION    = '+';
    private static final int CART_RIGHT      = '>';
    private static final int CART_LEFT       = '<';
    private static final int CART_UP         = '^';
    private static final int CART_DOWN       = 'v';

    private final List<String>       lines = Files.lines(Paths.get("src/main/resources/advent13.txt")).collect(Collectors.toList());
    private final int[][]            track = new int[this.lines.size()][];
    private final Set<Advent13.Cart> carts = new TreeSet<>();


    private Advent13() throws IOException {}

    public static void main(final String[] args) throws Exception {
        final Advent13 advent = new Advent13();

        advent.read();
//        advent.print();

        do advent.update(); while (advent.carts.stream().noneMatch(Advent13.Cart::isCrashed));

        final Advent13.Cart crashedCart = advent.carts.stream().filter(Advent13.Cart::isCrashed).findAny().get();
        System.out.printf("first crash location: (%3d,%3d)\n", crashedCart.getCol(), crashedCart.getRow());

        do advent.update(); while (advent.carts.stream().filter(cart -> !cart.isCrashed()).count() > 1);

        final Advent13.Cart lastCart = advent.carts.stream().filter(cart -> !cart.isCrashed()).findAny().get();
        System.out.printf("last cart location:   (%3d,%3d)", lastCart.getCol(), lastCart.getRow());
    }

    private List<Advent13.Cart> getOptionalCrashedCarts(final int[] coord) {
        return this.carts.stream().filter(cart -> !cart.isCrashed()).filter(cart -> cart.equals(new Advent13.Cart(coord[0], coord[1]))).collect(Collectors.toList());
    }

    private void print() {
        System.out.print("    ");
        for (int x = 0; x < this.track.length; x++) {
            if (x % 100 == 0) System.out.print(x / 100);
            else System.out.print(' ');
        }
        System.out.println();
        System.out.print("    ");
        for (int x = 0; x < this.track.length; x++) {
            if (x % 10 == 0) System.out.print(x % 100 / 10);
            else System.out.print(' ');
        }
        System.out.println();
        System.out.print("    ");
        for (int x = 0; x < this.track.length; x++) {
            System.out.print(x % 10);
        }
        System.out.println();

        for (int x = 0; x < this.track.length; x++) {
            System.out.printf("%4d", x);
            for (int y = 0; y < this.track[x].length; y++) {
                final List<Advent13.Cart> optionalCarts = this.getOptionalCrashedCarts(new int[]{x, y});
                if (optionalCarts.size() > 1) System.out.print("\033[1;31mX\u001B[0m");
                else if (optionalCarts.size() == 1) System.out.printf("\u001B[33m%s\u001B[0m", optionalCarts.get(0));
                else System.out.printf("%c", this.track[x][y]);
            }
            System.out.println();
        }
    }

    private void update() {
        final TreeSet<Advent13.Cart> remaining = this.carts.stream().filter(cart -> !cart.isCrashed()).collect(Collectors.toCollection(TreeSet::new));
        while (!remaining.isEmpty()) {
            final Advent13.Cart cart = remaining.pollFirst();
            if (cart != null && !cart.isCrashed()) {
                cart.update(this.track);
                final List<Advent13.Cart> potentialCrashed = this.getOptionalCrashedCarts(new int[]{cart.getRow(), cart.getCol()});
                if (potentialCrashed.size() > 1) potentialCrashed.forEach(Advent13.Cart::setCrashed);
            }
        }
    }

    private void read() {
        for (int row = 0; row < this.lines.size(); row++) {
            final char[] segments = this.lines.get(row).toCharArray();
            this.track[row] = new int[segments.length];

            for (int col = 0; col < segments.length; col++)
                if (segments[col] == Advent13.CART_RIGHT || segments[col] == Advent13.CART_LEFT || segments[col] == Advent13.CART_DOWN || segments[col] == Advent13.CART_UP) {
                    this.carts.add(new Advent13.Cart(row, col, segments[col]));
                    this.track[row][col] = segments[col] == Advent13.CART_RIGHT || segments[col] == Advent13.CART_LEFT ? Advent13.HORIZONTAL : Advent13.VERTICAL;
                } else this.track[row][col] = segments[col];
        }
    }

    static class Cart implements Comparable<Advent13.Cart> {
        private final int[]   change = {-1, 0, 1};
        private final String  id;
        private       int     pointer;
        private       int     row;
        private       int     col;
        private       int     direction;
        private       boolean crashed;

        Cart(final int row, final int col) {
            this.id = String.valueOf(row) + col;
            this.row = row;
            this.col = col;
        }

        Cart(final int row, final int col, final char direction) {
            this.id = String.valueOf(row) + col;
            this.row = row;
            this.col = col;
            this.setDirectionAsChar(direction);
        }

        void update(final int[][] track) {
            if (this.direction == 0) this.row--;
            else if (this.direction == 1) this.col++;
            else if (this.direction == 2) this.row++;
            else if (this.direction == 3) this.col--;
            else System.err.println("invalid state");
            this.updateDirection(track[this.row][this.col]);
        }

        private void updateDirection(final int next) {
            if (next == Advent13.CURVE_SLASH) this.setDirectionAsInt(this.direction % 2 == 0 ? this.direction + 1 : this.direction - 1);
            else if (next == Advent13.CURVE_BACKSLASH) this.setDirectionAsInt(this.direction % 2 == 0 ? this.direction - 1 : this.direction + 1);
            else if (next == Advent13.INTERSECTION) this.setDirectionAsInt(this.direction + this.change[this.pointer++ % this.change.length]);
        }

        @Override
        public String toString() {
            return String.valueOf(this.getDirection());
        }

        public int hashCode() {
            return this.id.hashCode();
        }

        public boolean equals(final Object obj) {
            if (obj instanceof Advent13.Cart) {
                final Advent13.Cart c2 = (Advent13.Cart) obj;
                return (this.row == c2.row) && (this.col == c2.col);
            }
            return super.equals(obj);
        }

        char getDirection() {
            if (this.direction == 0) return Advent13.CART_UP;
            else if (this.direction == 1) return Advent13.CART_RIGHT;
            else if (this.direction == 2) return Advent13.CART_DOWN;
            else if (this.direction == 3) return Advent13.CART_LEFT;
            System.err.println("invalid state");
            return '!';
        }

        void setDirectionAsChar(final char direction) {
            if (direction == Advent13.CART_UP) this.direction = 0;
            else if (direction == Advent13.CART_RIGHT) this.direction = 1;
            else if (direction == Advent13.CART_DOWN) this.direction = 2;
            else if (direction == Advent13.CART_LEFT) this.direction = 3;
        }

        private void setDirectionAsInt(final int direction) {
            this.direction = Math.floorMod(direction, 4);
        }

        int getCol() {
            return this.col;
        }

        int getRow() {
            return this.row;
        }

        String getId() {
            return this.id;
        }

        @SuppressWarnings("CompareToUsesNonFinalVariable")
        @Override
        public int compareTo(final @NotNull Advent13.Cart o) {
            int compare = Integer.compare(this.row, o.row);
            if (compare == 0) compare = Integer.compare(this.col, o.col);
            return compare;
        }

        void setCrashed() {
            this.crashed = true;
        }

        boolean isCrashed() {
            return this.crashed;
        }
    }
}
