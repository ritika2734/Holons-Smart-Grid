package com.htc.daoImpl;

import java.util.ArrayList;
import com.htc.dao.HolonObjectDao;
import com.htc.hibernate.pojo.Holon;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.utilities.HolonObjectHome;

/**
 * This class is the implementation of HolonObjectDao and calls respective functions of the next layer.
 */
public class HolonObjectDaoImpl implements HolonObjectDao {
	
	private HolonObjectHome holonObjectHome = new HolonObjectHome();

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonObjectDao#persist(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public Integer persist(HolonObject transientInstance) {
		return holonObjectHome.persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonObjectDao#merge(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public HolonObject merge(HolonObject detachedInstance) {
		return holonObjectHome.merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonObjectDao#findById(int)
	 */
	@Override
	public HolonObject findById(int holonObjectId) {
		return holonObjectHome.findById(holonObjectId);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonObjectDao#delete(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public boolean delete(HolonObject persistentInstance) {
		return holonObjectHome.delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonObjectDao#getAllHolonObject()
	 */
	@Override
	public ArrayList<HolonObject> getAllHolonObject() {
		return holonObjectHome.getAllHolonObject();
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonObjectDao#findByHolon(com.htc.hibernate.pojo.Holon)
	 */
	@Override
	public ArrayList<HolonObject> findByHolon(Holon holon) {
		return holonObjectHome.findByHolon(holon);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonObjectDao#findAllHolonCoordinators()
	 */
	@Override
	public ArrayList<HolonObject> findAllHolonCoordinators() {
		return holonObjectHome.findAllHolonCoordinators();
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonObjectDao#deleteAllHolonObjects()
	 */
	@Override
	public int deleteAllHolonObjects() {
		return holonObjectHome.deleteAllHolonObjects();
	}

}
