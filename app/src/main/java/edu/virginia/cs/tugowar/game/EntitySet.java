package edu.virginia.cs.tugowar.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by andy on 11/24/14.
 */
public class EntitySet extends Entity implements Set<Entity> {
    private List<Entity> list = new ArrayList<Entity>();
    private Set<Entity> set = new HashSet<Entity>();
    private Set<Entity> removed = new HashSet<Entity>();

    // Entity class overrides
    @Override
    protected void onUpdate(double delta) {
        for (Entity e : set) {
            if (e.isDestroyed()) {
                removed.add(e);
                continue;
            }
            e.update(delta);
            if (e.isDestroyed()) removed.add(e);
        }
        set.removeAll(removed);
        removed.clear();
    }

    @Override
    protected void onRender(Canvas canvas, Paint paint) {
        for (Entity e : set) {
            e.render(canvas, paint);
        }
    }

    @Override
    protected void onTouchEvent(MotionEvent event) {
        for (Entity e : set) {
            e.touchEvent(event);
        }
    }

    // Set interface overrides

    @Override
    public boolean add(Entity object) {
        return set.add(object);
    }

    @Override
    public boolean addAll(Collection<? extends Entity> collection) {
        return set.addAll(collection);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public boolean contains(Object object) {
        return set.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return set.containsAll(collection);
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public Iterator<Entity> iterator() {
        return set.iterator();
    }

    @Override
    public boolean remove(Object object) {
        return set.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return set.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return set.retainAll(collection);
    }

    @Override
    public int size() {
        return set.size();
    }


    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return set.toArray(array);
    }
}