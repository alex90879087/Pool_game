package Factory;

import Pool.BasicTable;
import Pool.Table;

// director of build pattern
public class BasicTableFactory implements TableFactory {

    @Override
    public Table create(Long x, Long y, double friction, String colour) {
        return new BasicTable(x, y, friction, colour);
    }
}
