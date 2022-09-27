package Factory;

import Pool.Table;

public interface TableFactory {

    Table create(Long x, Long y, double friction, String colour);
}
