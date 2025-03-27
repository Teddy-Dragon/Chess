package model;

import java.util.HashMap;
import java.util.List;

public record ListGame(
        HashMap<String, List<GameData>> gameList
) {
}
