package com.htc.serviceImpl;

import java.util.ArrayList;
import com.htc.hibernate.pojo.Holon;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.service.DaoAware;
import com.htc.service.HolonObjectService;

/**
 * This class is the implementation of interface HolonObjectService and calls respective functions of the next DAO layer.
 *
 */
public class HolonObjectServiceImpl extends DaoAware implements HolonObjectService {

	/* (non-Javadoc)
	 * @see com.htc.service.HolonObjectService#persist(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public Integer persist(HolonObject transientInstance) {
		return getHolonObjectDao().persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonObjectService#merge(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public HolonObject merge(HolonObject detachedInstance) {
		return getHolonObjectDao().merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonObjectService#findById(int)
	 */
	@Override
	public HolonObject findById(int holonObjectId) {
		return getHolonObjectDao().findById(holonObjectId);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonObjectService#delete(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public boolean delete(HolonObject persistentInstance) {
		return getHolonObjectDao().delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonObjectService#getAllHolonObject()
	 */
	@Override
	public ArrayList<HolonObject> getAllHolonObject() {
		return getHolonObjectDao().getAllHolonObject();
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonObjectService#findByHolon(com.htc.hibernate.pojo.Holon)
	 */
	@Override
	public ArrayList<HolonObject> findByHolon(Holon holon) {
		return getHolonObjectDao().findByHolon(holon);
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonObjectService#findAllHolonCoordinators()
	 */
	@Override
	public ArrayList<HolonObject> findAllHolonCoordinators() {
		return getHolonObjectDao().findAllHolonCoordinators();
	}

	/* (non-Javadoc)
	 * @see com.htc.service.HolonObjectService#deleteAllHolonObjects()
	 */
	@Override
	public int deleteAllHolonObjects() {
		return getHolonObjectDao().deleteAllHolonObjects();
	}

}
