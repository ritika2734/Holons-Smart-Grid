package com.htc.daoImpl;

import java.util.ArrayList;
import com.htc.dao.HolonElementStateDao;
import com.htc.hibernate.pojo.HolonElementState;
import com.htc.hibernate.utilities.HolonElementStateHome;

/**
 * This class is the implementation of HolonElementStateDao and calls respective functions of the next layer.
 */
public class HolonElementStateDaoImpl implements HolonElementStateDao {
	
	HolonElementStateHome holonElementStateHome = new HolonElementStateHome();

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementStateDao#persist(com.htc.hibernate.pojo.HolonElementState)
	 */
	@Override
	public Integer persist(HolonElementState transientInstance) {
		return holonElementStateHome.persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementStateDao#merge(com.htc.hibernate.pojo.HolonElementState)
	 */
	@Override
	public HolonElementState merge(HolonElementState detachedInstance) {
		return holonElementStateHome.merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementStateDao#findById(int)
	 */
	@Override
	public HolonElementState findById(int holonElementStateId) {
		return holonElementStateHome.findById(holonElementStateId);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementStateDao#delete(com.htc.hibernate.pojo.HolonElementState)
	 */
	@Override
	public boolean delete(HolonElementState persistentInstance) {
		return holonElementStateHome.delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementStateDao#getAllHolonElementState()
	 */
	@Override
	public ArrayList<HolonElementState> getAllHolonElementState() {
		return holonElementStateHome.getAllHolonElementState();
	}

}
