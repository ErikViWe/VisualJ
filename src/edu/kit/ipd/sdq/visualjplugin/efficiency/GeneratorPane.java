package edu.kit.ipd.sdq.visualjplugin.efficiency;

import edu.kit.ipd.sdq.visualj.util.Generator;

/**
 * A pane that contains a string generator.
 * 
 * @see Generator
 */
public interface GeneratorPane {
    
    public Generator<String> getGenerator();
}
