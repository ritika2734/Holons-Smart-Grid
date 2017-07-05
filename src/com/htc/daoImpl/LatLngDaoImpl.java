package com.htc.daoImpl;

import java.util.ArrayList;
import com.htc.dao.LatLngDao;
import com.htc.hibernate.pojo.LatLng;
import com.htc.hibernate.utilities.LatLngHome;

/**
 * This class is the implementation of LatLngDao and calls respective functions of the next layer.
 *
 */
public class LatLngDaoImpl implements LatLngDao {

	LatLngHome latLngHome = new LatLngHome();

	/* (non-Javadoc)
	 * @see com.htc.dao.LatLngDao#persist(com.htc.hibernate.pojo.LatLng)
	 */
	@Override
	public Integer persist(LatLng transientInstance) {
		return latLngHome.persist(transientInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.LatLngDao#merge(com.htc.hibernate.pojo.LatLng)
	 */
	@Override
	public LatLng merge(LatLng detachedInstance) {
		return latLngHome.merge(detachedInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.LatLngDao#findById(int)
	 */
	@Override
	public LatLng findById(int latLngId) {
		return latLngHome.findById(latLngId);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.LatLngDao#delete(com.htc.hibernate.pojo.LatLng)
	 */
	@Override
	public boolean delete(LatLng persistentInstance) {
		return latLngHome.delete(persistentInstance);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.LatLngDao#getAllLatLng()
	 */
	@Override
	public ArrayList<LatLng> getAllLatLng() {
		return latLngHome.getAllLatLng();
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.LatLngDao#findByLocation(java.lang.Double, java.lang.Double)
	 */
	@Override
	public ArrayList<LatLng> findByLocation(Double lat, Double lng) {
		return latLngHome.findByLocation(lat,lng);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.LatLngDao#findAllLatLngInsideTheCircle(java.lang.Double, java.lang.Double, java.lang.Double)
	 */
	@Override
	public ArrayList<LatLng> findAllLatLngInsideTheCircle(Double lat, Double lng, Double radius) {
		return latLngHome.findAllLatLngInsideTheCircle(lat,lng,radius);
	}

	/* (non-Javadoc)
	 * @see com.htc.dao.LatLngDao#deleteAllLatLngs()
	 */
	@Override
	public int deleteAllLatLngs() {
		return latLngHome.deleteAllLatLngs();
	}

}
