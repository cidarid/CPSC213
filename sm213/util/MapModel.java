package util;

import java.util.Observer;

public interface MapModel {
  void   addObserver (Observer anObserver);
  Object get         (Object key);
  Object reverseGet  (Object key);
}