package com.algaworks.algashop.ordering.domain.model.utility;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;
import io.hypersistence.tsid.TSID;
import io.hypersistence.tsid.TSID.Factory;
import java.util.UUID;

public class IdGenerator {

  private static final TimeBasedEpochRandomGenerator timeBasedEpochRandomGenerator =
      Generators.timeBasedEpochRandomGenerator();
  private static final TSID.Factory tsidFactory = Factory.INSTANCE;

  private IdGenerator() {
  }

  public static UUID generateTimeBasedUUID() {
    return timeBasedEpochRandomGenerator.generate();
  }

  public static TSID generateTimeBasedTsid() {
    return tsidFactory.generate();
  }

  public static TSID generateTSID() {
    return tsidFactory.generate();
  }

  public static TSID generateTSID(String value) {
    return TSID.from(value);
  }

  public static TSID generateTSID(Long value) {
    return TSID.from(value);
  }
}
