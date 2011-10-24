package edu.columbia.cs.ref.tool.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Set;

import edu.columbia.cs.ref.model.core.structure.OperableStructure;

public class CoreWriter {
	public static void writeOperableStructures(Set<OperableStructure> candidates, String output) throws IOException{
		ObjectOutput out = new ObjectOutputStream(new FileOutputStream(output));
		out.writeObject(candidates);
		out.close();
	}
}
