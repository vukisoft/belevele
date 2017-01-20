package hu.vuk.belevele.game.board;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import hu.vuk.belevele.game.stone.Stone;

public class NextStones {
  private final StoneFactory stoneFactory;

  private List<Stone> stones = Collections.emptyList();

  private int selected = -1;
  private Set<Stone> availables = Collections.emptySet();

  public NextStones(
      int count,
      StoneFactory stoneFactory) {
    this.stoneFactory = stoneFactory;
    initialize(count);
  }

  private void initialize(int count) {
    selected = 0;
    stones = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      stones.add(stoneFactory.create());
    }
  }

  public int getCount() {
    return stones.size();
  }

  public ImmutableList<Stone> getAll() {
    return ImmutableList.copyOf(stones);
  }

  public Stone getSelected() {
    return selected >= 0 ? stones.get(selected) : null;
  }

  public void setAvailabes(Set<Stone> availables) {
    this.availables = availables;
    if (selected == -1 || !availables.contains(getSelected())) {
      selected = Iterables.indexOf(stones, stone -> availables.contains(stone));
    }
  }

  public boolean setSelected(int selected) {
    if (selected < 0 || selected >= stones.size()
        || !availables.contains(stones.get(selected))) {
      return false;
    }

    this.selected = selected;
    return true;
  }

  public void rollSelected() {
    stones.set(selected, stoneFactory.create());
  }

  public boolean hasNext() {
    return selected >= 0;
  }

  public boolean hasAvailable() {
    return !availables.isEmpty();
  }

  public boolean isAvailable(int index) {
    return availables.contains(stones.get(index));
  }

  public int getSelectedIndex() {
    return selected;
  }

  public Stone get(int index) {
    return stones.get(index);
  }
}
