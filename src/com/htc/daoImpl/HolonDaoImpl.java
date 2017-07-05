package com.htc.daoImpl;

import java.util.ArrayList;
import com.htc.dao.HolonDao;
import com.htc.hibernate.pojo.Holon;
import com.htc.hibernate.utilities.HolonHome;

/**
 * This class is the implementation of HolonDao and calls respective functions of the next layer.
 *
 */
public class HolonDaoImpl implements HolonDao {
	private HolonHome holonHome = new HolonHome();

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonDao#persist(com.htc.hibernate.pojo.Holon)
	 */
	@Override
	public Integer persist(Holon transientInstance) {
		return holonHome.persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonDao#merge(com.htc.hibernate.pojo.Holon)
	 */
	@Override
	public Holon merge(Holon detachedInstance) {
		return holonHome.merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonDao#findById(int)
	 */
	@Override
	public Holon findById(int holonId) {
		return holonHome.findById(holonId);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonDao#delete(com.htc.hibernate.pojo.Holon)
	 */
	@Override
	public boolean delete(Holon persistentInstance) {
		return holonHome.delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonDao#getAllHolon()
	 */
	@Override
	public ArrayList<Holon> getAllHolon() {
		return holonHome.getAllHolon();
	}

}
