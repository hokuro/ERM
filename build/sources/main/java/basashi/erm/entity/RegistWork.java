package basashi.erm.entity;

public class RegistWork {
	public int tickCount;
	public IERMAI works;

	public boolean checkAI(IERMAI ai){
		return works.equals(ai);
	}
}
