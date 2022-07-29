package Stages;

import Simulation.Simulator;

public abstract class Stages
{
    Simulator simulator;

    public Stages(Simulator simulator) { this.simulator = simulator; }

    public abstract void setToPipeline();
}

