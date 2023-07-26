package Repositories;
import java.util.HashMap;
import java.util.Map;

import Objects.Tool;
import Objects.ToolType;

public class ToolRepository {
    private static ToolRepository toolRepository = new ToolRepository();
    private Map<String, Tool> tools;

    private ToolRepository() {
        // Add all tools to tool repository (in real world, would use db calls instead of a singleton class)
        tools = new HashMap<String, Tool>();
        ToolType toolType = new ToolType("Chainsaw", 1.49f, true, false, true);
        tools.put("CHNS", new Tool("CHNS", toolType, "Stihl"));

        toolType = new ToolType("Ladder", 1.99f, true, true, false);
        tools.put("LADW", new Tool("LADW", toolType, "Werner"));

        toolType = new ToolType("Jackhammer", 2.99f, true, false, false);
        tools.put("JAKD", new Tool("JAKD", toolType, "DeWalt"));
        tools.put("JAKD", new Tool("JAKR", toolType, "Ridgid"));
    }

    public static ToolRepository getInstance() {
        return toolRepository;
    }

    public Tool getTool(String toolCode) {
        return tools.get(toolCode);
    }
}
