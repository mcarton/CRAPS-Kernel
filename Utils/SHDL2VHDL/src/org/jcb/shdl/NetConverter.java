package org.jcb.shdl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.jcb.shdl.netc.java.*;

import mg.egg.eggc.libjava.EGGException;
import mg.egg.eggc.libjava.Options;

public class NetConverter {
	
	private File file;
	private PrintStream msgStream;
	
	public NetConverter(File file, PrintStream msgStream) {
		this.file = file;
		this.msgStream = msgStream;
	}
	
	public File start() {
		String[] args = new String[] { file.getAbsolutePath() };
		msgStream.println("-- state diagram conversion " + file.getName());
		Options opts = new Options(args);
		NET compilo = null;
		try {
			opts.analyse();
			compilo = new NET(opts);
			compilo.compile() ;
			// diagram = synthetized attribute of axiom DIAGRAM
			NETStateDiagram diagram = compilo.get_diagram();
			
			NETInterface interf = diagram.getInterface();
			ArrayList inputSignals = interf.getInputs().getSignals();
			ArrayList outputSignals = interf.getOutputs().getSignals();
			
			// sépare les statements en trois groupes
			ArrayList<NETTransition> allTransitions = new ArrayList<NETTransition>();
			ArrayList<NETMooreOutputs> allMooreOutputs = new ArrayList<NETMooreOutputs>();
			ArrayList<String> allAdded = new ArrayList<String>();
			ArrayList statements = diagram.getStatements().getStatements();
			for (int i = 0; i < statements.size(); i++) {
				NETStatement statement = (NETStatement) statements.get(i);
				if (statement.getTransition() != null) {
					allTransitions.add(statement.getTransition());
				} else if (statement.getMooreOutputs() != null) {
					allMooreOutputs.add(statement.getMooreOutputs());
				} else if (statement.getAdded() != null) {
					allAdded.add(statement.getAdded());
				}
			}
			
			// construit la liste des états référencés
			// regarde aussi s'il existe des MooreOutput
			ArrayList<String> allStates = new ArrayList<String>();
			boolean mooreOutputsExist = false;
			for (int i = 0; i < allTransitions.size(); i++) {
				NETTransition transition = allTransitions.get(i);
				if (!allStates.contains(transition.getSrc())) allStates.add(transition.getSrc());
				if (!allStates.contains(transition.getDest())) allStates.add(transition.getDest());
			}
			for (int i = 0; i < allMooreOutputs.size(); i++) {
				mooreOutputsExist = true;
				NETMooreOutputs mooreOutputs = allMooreOutputs.get(i);
				if (!allStates.contains(mooreOutputs.getState())) allStates.add(mooreOutputs.getState());
			}
			
			// vérifie qu'on est en pur MOORE ou pur MEALY
			boolean errorMooreMealy = false;
			if (mooreOutputsExist) {
				// il existe des clauses de Moore: vérifie qu'il n'y a aucune clause de Mealy
				for (int i = 0; i < allTransitions.size(); i++) {
					NETTransition transition = allTransitions.get(i);
					if (transition.getAffectations() != null) {
						errorMooreMealy = true;
						break;
					}
				}
			} else {
				// il n'existe pas de clause de Moore: vérifie que toutes les transitions ont des sorties Mealy
				for (int i = 0; i < allTransitions.size(); i++) {
					NETTransition transition = allTransitions.get(i);
					if (transition.getAffectations() == null) {
						errorMooreMealy = true;
						break;
					}
				}
			}
			if (errorMooreMealy) {
				msgStream.println("** la description n'est ni totalement MOORE, ni totalement MEALY");
				return null;
			}
			
			if (mooreOutputsExist) {
				// système de MOORE: vérification
				// on vérifie que chaque état référencé a une clause 'outputs' et une seule
				ArrayList states = (ArrayList) allStates.clone();
				
				for (int i = 0; i < allMooreOutputs.size(); i++) {
					NETMooreOutputs mooreOutputs = allMooreOutputs.get(i);
					if (states.contains(mooreOutputs.getState())) {
						states.remove(mooreOutputs.getState());
						// on vérifie qu'il y a des affectations pour chaque sortie
						ArrayList outs = (ArrayList) outputSignals.clone();
						ArrayList affectations = mooreOutputs.getAffectations().getAffectations();
						for (int j = 0; j < affectations.size(); j++) {
							NETAffectation aff = (NETAffectation) affectations.get(j);
							NETSignal signal = aff.getSignal();
							if (outs.contains(signal)) {
								outs.remove(signal);
							} else {
								msgStream.println("** graphe de MOORE, clause 'outputs' de l'état " + mooreOutputs.getState() + " : il y a un problème avec la sortie " + signal.getNormalizedName());
							}
						}
						if (outs.size() > 0) {
							msgStream.println("** graphe de MOORE, clause 'outputs' de l'état " + mooreOutputs.getState() + " : il manque l'affectation des signaux de sortie suivants : " + outs);
							return null;
						}
					} else {
						msgStream.println("** graphe de MOORE : l'état " + mooreOutputs.getState() + ", possède plusieurs clauses 'outputs'");
					}
				}
				if (states.size() > 0) {
					msgStream.println("** graphe de MOORE : les états suivants ne possèdent pas de clauses 'outputs' : " + states);
					return null;
				}
			} else {
				// système de MEALY: vérification
				// on vérifie que pour chaque transition, toutes les sorties sont affectées
				for (int i = 0; i < allTransitions.size(); i++) {
					NETTransition transition = allTransitions.get(i);
					ArrayList affectations = transition.getAffectations().getAffectations();
					ArrayList outs = (ArrayList) outputSignals.clone();
					for (int j = 0; j < affectations.size(); j++) {
						NETAffectation aff = (NETAffectation) affectations.get(j);
						NETSignal signal = aff.getSignal();
						if (outs.contains(signal)) {
							outs.remove(signal);
						} else {
							msgStream.println("** graphe de MEALY, clause 'outputs' de la transition " + transition.getSrc() + " -> " + transition.getDest() + " : il y a un problème avec la sortie " + signal.getNormalizedName());
							return null;
						}
					}
					if (outs.size() > 0) {
						msgStream.println("** graphe de MEALY, clause 'outputs' de la transition " + transition.getSrc() + " -> " + transition.getDest() + " : il manque l'affectation des signaux de sortie suivants : " + outs);
						return null;
					}
				}
			}

			// calcul du nombre de bits nécessaires
			int nbBits = (int) (Math.log(allStates.size())/Math.log(2.0));
			if ((1 << nbBits) < allStates.size()) nbBits += 1;
			msgStream.println("-- " + allStates.size() + " états -> codage de l'état sur " + nbBits + " bits");
			
			String prefix = file.getName();
			prefix = prefix.substring(0, prefix.lastIndexOf(".net"));
			File outFile = new File(file.getParent(), prefix + ".shd");
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileOutputStream(outFile));
			} catch(IOException ex) {
				msgStream.println("** impossible to open '" + outFile + "' for writing");
				return null;
			}
			
			msgStream.println("-- creating " + outFile.getName() + " ...");
			pw.println();
			pw.println("// automatically generated from state diagram description: " + file.getAbsolutePath());
			pw.println();
			pw.print("module " + interf.getModuleName() + "(" +
					interf.getReset().getSignal() + ", " +
					interf.getClock().getSignal());
			for (int i = 0; i < inputSignals.size(); i++) {
				NETSignal sig = (NETSignal) inputSignals.get(i);
				pw.print(", " + sig);
			}
			pw.print(" : ");
			for (int i = 0; i < outputSignals.size(); i++) {
				NETSignal sig = (NETSignal) outputSignals.get(i);
				if (i > 0) pw.print(", ");
				pw.print(sig);
			}
			ArrayList addedOutputSignals = interf.getAddedOutputs().getSignals();
			for (int i = 0; i < addedOutputSignals.size(); i++) {
				NETSignal sig = (NETSignal) addedOutputSignals.get(i);
				pw.print(", " + sig);
			}
			pw.println(")");
			pw.println();

			// registre d'état
			pw.println("    // " + nbBits + " bits state register");
			pw.println("    state[" + (nbBits-1) + "..0] := stateD[" + (nbBits-1) + "..0] ;");
			pw.println("    state[" + (nbBits-1) + "..0].clk = " + interf.getClock() + " ;");
			pw.println("    state[" + (nbBits-1) + "..0].rst = " + interf.getReset() + " ;");
			pw.println();
			
			pw.println("    // states assignation");
			// assignation des états: code = indice de l'état dans allStates
			// calcul des codes en binaire
			StringBuffer[] codes = new StringBuffer[allStates.size()];
			for (int i = 0; i < allStates.size(); i++) {
				String state = (String) allStates.get(i);
				StringBuffer code = new StringBuffer();
				int c = i; // code = rang dans allState
				for (int j = 0; j < nbBits; j++) {
					int r = c % 2;
					code.insert(0, r);
					c = c / 2;
				}
				codes[i] = code;
			}
			for (int i = 0; i < allStates.size(); i++) {
				String state = (String) allStates.get(i);
				pw.print("    is_" + state + " = ");
				StringBuffer code = codes[i];
				for (int j = 0; j < nbBits; j++) {
					if (j > 0) pw.print("*");
					char r = code.charAt(j);
					if (r == '0') pw.print("/");
					pw.print("state[" + (nbBits-j-1) + "]");
				}
				pw.println(" ; // " + state + " = 0b" + code);
			}
			pw.println();
			
			// changements d'états
			pw.println("    // state transitions");
			pw.println("    stateD[" + (nbBits-1) + "..0] = ");
			for (int i = 0; i < allTransitions.size(); i++) {
				NETTransition transition = allTransitions.get(i);
				int idxDest = allStates.indexOf(transition.getDest());
				NETTermsSum condition = transition.getCondition();
				if (condition.toString().equals("1")) {
					pw.print("        is_" + transition.getSrc() + "*0b" + codes[idxDest]);
				} else {
					pw.print("        ");
					for (int j = 0; j < condition.getTerms().size(); j++) {
						NETTerm term = (NETTerm) condition.getTerms().get(j);
						if (j > 0) pw.print("+");
						pw.print("is_" + transition.getSrc() + "*" + term.getWrittenForm() + "*0b" + codes[idxDest]);
					}
				}
				if (i < allTransitions.size() - 1) pw.println(" +"); else pw.println(" ;");
			}
			pw.println();	
			
			// équations des sorties
			if (mooreOutputsExist) {
				// MOORE
				pw.println("    // MOORE outputs");
				for (int i = 0; i < outputSignals.size(); i++) {
					NETSignal signal = (NETSignal) outputSignals.get(i);
					pw.println();
					pw.println("    " + signal + " =");
					int nli = 0;
					for (int j = 0; j < allMooreOutputs.size(); j++) {
						NETMooreOutputs mooreOutputs = allMooreOutputs.get(j);
						ArrayList affectations = mooreOutputs.getAffectations().getAffectations();
						for (int k = 0; k < affectations.size(); k++) {
							NETAffectation aff = (NETAffectation) affectations.get(k);
							NETSignal sig = aff.getSignal();
							if (!sig.equals(signal)) continue;
							ArrayList terms = aff.getTermsSum().getTerms();
							// enlève les sommes de termes nulles
							if (aff.getTermsSum().isZeros()) continue;
							if (nli > 0) pw.println(" +");
							nli += 1;
							pw.print("        ");
							for (int l = 0; l < terms.size(); l++) {
								if (l > 0) pw.print("+");
								NETTerm term = (NETTerm) terms.get(l);
								pw.print("is_" + mooreOutputs.getState() + "*" + term.getWrittenForm());
							}
							break;
						}
					}
					if (nli == 0) pw.print("        0");
					pw.println(" ;");
				}
			} else {
				// MEALY
				pw.println("    // MEALY outputs");
				for (int i = 0; i < outputSignals.size(); i++) {
					NETSignal signal = (NETSignal) outputSignals.get(i);
					pw.println();
					pw.println("    " + signal + " =");
					int nli = 0;
					for (int j = 0; j < allTransitions.size(); j++) {
						NETTransition transition = allTransitions.get(j);
						ArrayList affectations = transition.getAffectations().getAffectations();
						
						NETTermsSum condition = transition.getCondition();
						for (int k = 0; k < affectations.size(); k++) {
							NETAffectation aff = (NETAffectation) affectations.get(k);
							NETSignal sig = aff.getSignal();
							if (!sig.equals(signal)) continue;
							// enlève les sommes de termes nulles
							if (aff.getTermsSum().isZeros()) continue;
							if (nli > 0) pw.println(" +");
							nli += 1;

							if (condition.toString().equals("1")) {
								pw.print("        is_" + transition.getSrc() + "*" + aff.getTermsSum());
							} else {
								pw.print("        ");
								for (int l = 0; l < condition.getTerms().size(); l++) {
									NETTerm term = (NETTerm) condition.getTerms().get(l);
									if (l > 0) pw.print("+");
									pw.print("is_" + transition.getSrc() + "*" + term.getWrittenForm() + "*" + aff.getTermsSum());
								}
							}
						}

//						// élimine la condition '1' (always)
//						if (condition.equals("1")) condition = ""; else condition = "*" + condition;
//						for (int k = 0; k < affectations.size(); k++) {
//							NETAffectation aff = (NETAffectation) affectations.get(k);
//							NETSignal sig = aff.getSignal();
//							if (!sig.equals(signal)) continue;
//							// enlève les sommes de termes nulles
//							if (aff.getTermsSum().isZeros()) continue;
//							if (nli > 0) pw.println(" +");
//							nli += 1;
//							pw.print("        is_" + transition.getSrc() + condition + "*" + aff.getTermsSum());
//							break;
//						}
					}
					if (nli == 0) pw.print("        0");
					pw.println(" ;");
				}
			}
			pw.println();
			
			// additional
			if (allAdded.size() > 0) {
				pw.println("    // additional statements");
				for (int i = 0; i < allAdded.size(); i++) {
					String line = allAdded.get(i);
					pw.println("    " + line.trim());
				}
			}

			pw.println("end module");
			
			// flushing and closing
			pw.flush();
			pw.close();
			
			// return created file
			return outFile;

		} catch (EGGException e) {
			// display error messages
			if (e.getLine() == -1)
				msgStream.println("** " + file.getName() + " : parse error, " + e.getMsg());
			else
				msgStream.println("** " + file.getName() + ":" + e.getLine() + " : parse error, " + e.getMsg());
			return null;
		} catch(Exception e){
			e.printStackTrace();
			msgStream.println("** unknown internal error e=" + e);
			return null;
		} finally {
			try {
				compilo.get_scanner().contexte.source.close() ;
			} catch(Exception ex) {
				msgStream.println("** could not close source file: " + file);
				return null;
			}
		}
	}

}
