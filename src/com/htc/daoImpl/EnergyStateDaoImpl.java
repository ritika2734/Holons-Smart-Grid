package com.htc.daoImpl;

import java.util.ArrayList;
import com.htc.dao.EnergyStateDao;
import com.htc.hibernate.pojo.EnergyState;
import com.htc.hibernate.utilities.EnergyStateHome;

/**
 * This class is the implementation of EnergyStateDao and calls respective functions of the next layer.
 *
 */
public class EnergyStateDaoImpl implements EnergyStateDao{
	
	EnergyStateHome energyStateHome = new EnergyStateHome();

	/* (non-Javadoc)
	 * @see com.htc.dao.EnergyStateDao#findById(int)
	 */
	@Override
	public EnergyState findById(int energyStateId) {
		// TODO Auto-generated method stub
		return energyStateHome.findById(energyStateId);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.EnergyStateDao#getAllEnergyState()
	 */
	@Override
	public ArrayList<EnergyState> getAllEnergyState() {
		// TODO Auto-generated method stub
		return energyStateHome.getAllEnergyState();
	}

}
