package com.algaworks.algashop.ordering.domain.utility;

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

  public static UUID generateTimeBasedUuid() {
    return timeBasedEpochRandomGenerator.generate();
  }

  public static TSID generateTimeBasedTsid() {
    return tsidFactory.generate();
  }
}
