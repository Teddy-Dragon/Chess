package model;

import java.util.HashMap;
import java.util.List;

public record ListModel(
        HashMap<String, List<GameData>> gameList
) {
}
