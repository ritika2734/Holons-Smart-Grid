package com.htc.daoImpl;

import java.util.ArrayList;
import com.htc.dao.HolonObjectTypeDao;
import com.htc.hibernate.pojo.HolonObjectType;
import com.htc.hibernate.utilities.HolonObjectTypeHome;

/**
 * This class is the implementation of HolonObjectTypeDao and calls respective functions of the next layer.
 *
 */
public class HolonObjectTypeDaoImpl implements HolonObjectTypeDao {
	
	HolonObjectTypeHome holonObjectTypeHome = new HolonObjectTypeHome();

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonObjectTypeDao#persist(com.htc.hibernate.pojo.HolonObjectType)
	 */
	@Override
	public Integer persist(HolonObjectType transientInstance) {
		return holonObjectTypeHome.persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonObjectTypeDao#merge(com.htc.hibernate.pojo.HolonObjectType)
	 */
	@Override
	public HolonObjectType merge(HolonObjectType detachedInstance) {
		return holonObjectTypeHome.merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonObjectTypeDao#findById(int)
	 */
	@Override
	public HolonObjectType findById(int holonObjectTypeId) {
		return holonObjectTypeHome.findById(holonObjectTypeId);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonObjectTypeDao#delete(com.htc.hibernate.pojo.HolonObjectType)
	 */
	@Override
	public boolean delete(HolonObjectType persistentInstance) {
		return holonObjectTypeHome.delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonObjectTypeDao#getAllHolonObjectType()
	 */
	@Override
	public ArrayList<HolonObjectType> getAllHolonObjectType() {
		return holonObjectTypeHome.getAllHolonObjectType();
	}

}
