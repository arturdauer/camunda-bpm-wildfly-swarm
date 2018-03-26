package org.wildfly.swarm.camunda.bpm;

import org.wildfly.swarm.config.CamundaBpmPlatform;
import org.wildfly.swarm.config.camunda.bpm.platform.JobExecutor;
import org.wildfly.swarm.config.camunda.bpm.platform.ProcessEngines;
import org.wildfly.swarm.config.camunda.bpm.platform.job_executor.JobAcquisitions;
import org.wildfly.swarm.spi.api.Fraction;
import org.wildfly.swarm.spi.api.annotations.MarshalDMR;
import org.wildfly.swarm.spi.api.annotations.WildFlyExtension;
import org.wildfly.swarm.spi.api.annotations.WildFlySubsystem;

/**
 * @author Svetlana Dorokhova.
 */
@MarshalDMR
@WildFlyExtension(module = "org.camunda.bpm.wildfly.camunda-wildfly-subsystem")
@WildFlySubsystem("camunda-bpm-platform")
public class CamundaBpmnEngineFraction extends CamundaBpmPlatform<CamundaBpmnEngineFraction>
  implements Fraction<CamundaBpmnEngineFraction> {

  private static final String DEFAULT = "default";

  /**
   * Create the default fraction.
   *
   * @return The configured fraction.
   */
  public static CamundaBpmnEngineFraction createDefaultFraction() {
    CamundaBpmnEngineFraction fraction = new CamundaBpmnEngineFraction();
    return fraction.applyDefaults();
  }

  @Override
  public CamundaBpmnEngineFraction applyDefaults() {
    jobExecutor(
      new JobExecutor<>(DEFAULT)
        .jobAcquisitions(
          new JobAcquisitions<>(DEFAULT)
            .name(DEFAULT)
            .property("lockTimeInMillis", "300000")
            .property("waitTimeInMillis", "5000")
            .property("maxJobsPerAcquisition", "3"))
        .coreThreads(3)
        .maxThreads(5)
        .queueLength(10));

//    Map<String, Map> spinPlugin = new HashMap<>();
//    spinPlugin.put("org.camunda.spin.plugin.impl.SpinProcessEnginePlugin", null);
//
//    Map<String, Map> connectPlugin = new HashMap<>();
//    connectPlugin.put("org.camunda.connect.plugin.impl.ConnectProcessEnginePlugin", null);

    processEngines(
      new ProcessEngines<>(DEFAULT)
        .name(org.camunda.bpm.engine.ProcessEngines.NAME_DEFAULT)
        .attributeDefault(Boolean.TRUE)
        .datasource("java:jboss/datasources/ProcessEngine")
        .historyLevel("full")
        .property("jobExecutorAcquisitionName", "default")
        .property("isAutoSchemaUpdate", "true")
        .property("authorizationEnabled", "true")
        .property("jobExecutorDeploymentAware", "true")
        .property("historyCleanupBatchWindowStartTime", "00:01")
//      .plugins(spinPlugin, connectPlugin)
    );
    return this;
  }

}
