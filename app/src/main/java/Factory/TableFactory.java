package Factory;

import Pool.Table;

public interface TableFactory {

    Table create(double x, double y, double friction, String colour);
}
