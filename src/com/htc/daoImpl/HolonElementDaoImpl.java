package com.htc.daoImpl;

import java.util.ArrayList;
import com.htc.dao.HolonElementDao;
import com.htc.hibernate.pojo.HolonElement;
import com.htc.hibernate.pojo.HolonObject;
import com.htc.hibernate.utilities.HolonElementHome;

/**
 * This class is the implementation of HolonElementDao and calls respective functions of the next layer.
 */
public class HolonElementDaoImpl implements HolonElementDao {

	HolonElementHome holonElementHome = new HolonElementHome();
	
	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementDao#persist(com.htc.hibernate.pojo.HolonElement)
	 */
	@Override
	public Integer persist(HolonElement transientInstance) {
		return holonElementHome.persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementDao#merge(com.htc.hibernate.pojo.HolonElement)
	 */
	@Override
	public HolonElement merge(HolonElement detachedInstance) {
		return holonElementHome.merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementDao#findById(int)
	 */
	@Override
	public HolonElement findById(int holonElementId) {
		return holonElementHome.findById(holonElementId);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementDao#delete(com.htc.hibernate.pojo.HolonElement)
	 */
	@Override
	public boolean delete(HolonElement persistentInstance) {
		return holonElementHome.delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementDao#getAllHolonElement()
	 */
	@Override
	public ArrayList<HolonElement> getAllHolonElement() {
		return holonElementHome.getAllHolonElement();
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementDao#getHolonElements(com.htc.hibernate.pojo.HolonObject)
	 */
	@Override
	public ArrayList<HolonElement> getHolonElements(HolonObject holonObject) {
		return holonElementHome.getHolonElements(holonObject);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.HolonElementDao#deleteAllHolonElements()
	 */
	@Override
	public int deleteAllHolonElements() {
		return holonElementHome.deleteAllHolonElements();
	}

}
