/*
 * Copyright 2014 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.realm;

import android.test.AndroidTestCase;

import io.realm.entities.Cat;
import io.realm.entities.Dog;
import io.realm.entities.Owner;

public class RealmLinkTests extends AndroidTestCase {

    protected Realm testRealm;

    protected void setUp() {
        Realm.deleteRealmFile(getContext());
        testRealm = Realm.getInstance(getContext());

        testRealm.beginTransaction();
        testRealm.clear(Dog.class);
        testRealm.clear(Owner.class);

        Dog dog1 = testRealm.createObject(Dog.class);
        dog1.setName("Pluto");
        dog1.setAge(5);
        dog1.setHeight(1.2f);
        dog1.setWeight(9.9);
        dog1.setHasTail(true);

        Dog dog2 = testRealm.createObject(Dog.class);
        dog2.setName("Fido");
        dog2.setAge(10);
        dog2.setHeight(0.7f);
        dog2.setWeight(11.3);
        dog2.setHasTail(true);

        Cat cat = testRealm.createObject(Cat.class);
        cat.setName("Blackie");
        cat.setAge(12);
        cat.setHeight(0.3f);
        cat.setWeight(1.1);
        cat.setHasTail(true);

        Owner owner = testRealm.createObject(Owner.class);
        owner.setName("Tim");
        owner.getDogs().add(dog1);
        owner.getDogs().add(dog2);
        owner.setCat(cat);

        testRealm.commitTransaction();
    }

    public void testObjects() {
        RealmResults<Owner> owners = testRealm.allObjects(Owner.class);
        assertEquals(1, owners.size());
        assertEquals(2, owners.first().getDogs().size());
        assertEquals("Pluto", owners.first().getDogs().first().getName());
        assertEquals("Fido", owners.first().getDogs().last().getName());
        assertEquals("Blackie", owners.first().getCat().getName());
        assertEquals(12, owners.first().getCat().getAge());
    }

    public void testQuerySingleRelationBoolean() {
        RealmResults<Owner> owners = testRealm.where(Owner.class).equalTo("cat.hasTail", true).findAll();
        assertEquals(1, owners.size());
        assertEquals(12, owners.first().getCat().getAge());

        RealmResults<Owner> none = testRealm.where(Owner.class).equalTo("cat.hasTail", false).findAll();
        assertEquals(0, none.size());
    }

    public void testQuerySingleRelationInteger() {
        RealmResults<Owner> owners1 = testRealm.where(Owner.class).equalTo("cat.age", 12).findAll();
        assertEquals(1, owners1.size());
        assertEquals(12, owners1.first().getCat().getAge());

        RealmResults<Owner> none1 = testRealm.where(Owner.class).equalTo("cat.age", 13).findAll();
        assertEquals(0, none1.size());

        RealmResults<Owner> owners2 = testRealm.where(Owner.class).notEqualTo("cat.age", 13).findAll();
        assertEquals(1, owners2.size());
        assertEquals(12, owners2.first().getCat().getAge());

        RealmResults<Owner> none2 = testRealm.where(Owner.class).notEqualTo("cat.age", 12).findAll();
        assertEquals(0, none2.size());

        RealmResults<Owner> owners3 = testRealm.where(Owner.class).greaterThan("cat.age", 5).findAll();
        assertEquals(1, owners3.size());
        assertEquals(12, owners3.first().getCat().getAge());

        RealmResults<Owner> owners4 = testRealm.where(Owner.class).greaterThanOrEqualTo("cat.age", 5).findAll();
        assertEquals(1, owners4.size());
        assertEquals(12, owners4.first().getCat().getAge());

        RealmResults<Owner> owners5 = testRealm.where(Owner.class).lessThan("cat.age", 20).findAll();
        assertEquals(1, owners5.size());
        assertEquals(12, owners5.first().getCat().getAge());

        RealmResults<Owner> owners6 = testRealm.where(Owner.class).lessThanOrEqualTo("cat.age", 20).findAll();
        assertEquals(1, owners6.size());
        assertEquals(12, owners6.first().getCat().getAge());

        RealmResults<Owner> owners7 = testRealm.where(Owner.class).between("cat.age", 1, 20).findAll();
        assertEquals(1, owners7.size());
        assertEquals(12, owners7.first().getCat().getAge());
    }

    public void testQuerySingleRelationFloat() {
        RealmResults<Owner> owners1 = testRealm.where(Owner.class).greaterThanOrEqualTo("cat.height", 0.2f).findAll();
        assertEquals(1, owners1.size());
        assertEquals(12, owners1.first().getCat().getAge());

        RealmResults<Owner> owners2 = testRealm.where(Owner.class).greaterThan("cat.height", 0.2f).findAll();
        assertEquals(1, owners2.size());
        assertEquals(12, owners2.first().getCat().getAge());

        RealmResults<Owner> owners3 = testRealm.where(Owner.class).lessThan("cat.height", 2.2f).findAll();
        assertEquals(1, owners3.size());
        assertEquals(12, owners3.first().getCat().getAge());

        RealmResults<Owner> owners4 = testRealm.where(Owner.class).lessThanOrEqualTo("cat.height", 2.2f).findAll();
        assertEquals(1, owners4.size());
        assertEquals(12, owners4.first().getCat().getAge());

        RealmResults<Owner> owners5 = testRealm.where(Owner.class).notEqualTo("cat.height", 0.2f).findAll();
        assertEquals(1, owners5.size());
        assertEquals(12, owners5.first().getCat().getAge());

        RealmResults<Owner> owners6 = testRealm.where(Owner.class).greaterThanOrEqualTo("cat.height", 0.3f).findAll();
        assertEquals(1, owners6.size());
        assertEquals(12, owners6.first().getCat().getAge());

        RealmResults<Owner> owners7 = testRealm.where(Owner.class).between("cat.height", 0.2f, 2.2f).findAll();
        assertEquals(1, owners7.size());
        assertEquals(12, owners7.first().getCat().getAge());
    }

    public void testQuerySingleRelationDouble() {
        RealmResults<Owner> owners1 = testRealm.where(Owner.class).greaterThanOrEqualTo("cat.weight", 0.2).findAll();
        assertEquals(1, owners1.size());
        assertEquals(12, owners1.first().getCat().getAge());

        RealmResults<Owner> owners2 = testRealm.where(Owner.class).greaterThan("cat.weight", 0.2).findAll();
        assertEquals(1, owners2.size());
        assertEquals(12, owners2.first().getCat().getAge());

        RealmResults<Owner> owners3 = testRealm.where(Owner.class).lessThan("cat.weight", 2.2).findAll();
        assertEquals(1, owners3.size());
        assertEquals(12, owners3.first().getCat().getAge());

        RealmResults<Owner> owners4 = testRealm.where(Owner.class).lessThanOrEqualTo("cat.weight", 2.2).findAll();
        assertEquals(1, owners4.size());
        assertEquals(12, owners4.first().getCat().getAge());

        RealmResults<Owner> owners5 = testRealm.where(Owner.class).notEqualTo("cat.weight", 0.2).findAll();
        assertEquals(1, owners5.size());
        assertEquals(12, owners5.first().getCat().getAge());

        RealmResults<Owner> owners6 = testRealm.where(Owner.class).greaterThanOrEqualTo("cat.weight", 0.3).findAll();
        assertEquals(1, owners6.size());
        assertEquals(12, owners6.first().getCat().getAge());

        RealmResults<Owner> owners7 = testRealm.where(Owner.class).between("cat.weight", 0.2, 2.2).findAll();
        assertEquals(1, owners7.size());
        assertEquals(12, owners7.first().getCat().getAge());
    }


    public void testQuerySingleRelationString() {
        RealmResults<Owner> owners = testRealm.where(Owner.class).equalTo("cat.name", "Blackie").findAll();
        assertEquals(1, owners.size());

        RealmResults<Owner> none = testRealm.where(Owner.class).equalTo("cat.name", "Max").findAll();
        assertEquals(0, none.size());
    }

    public void testQueryMultipleRelationsBoolean() {
        RealmResults<Owner> owners = testRealm.where(Owner.class).equalTo("dogs.hasTail", true).findAll();
        assertEquals(1, owners.size());

        RealmResults<Owner> none = testRealm.where(Owner.class).notEqualTo("dogs.hasTail", true).findAll();
        assertEquals(0, none.size());
    }

    public void testQueryMultipleRelationsInteger() {
        RealmResults<Owner> owners1 = testRealm.where(Owner.class).equalTo("dogs.age", 10).findAll();
        assertEquals(1, owners1.size());

        RealmResults<Owner> none1 = testRealm.where(Owner.class).equalTo("dogs.age", 7).findAll();
        assertEquals(0, none1.size());

        RealmResults<Owner> owners2 = testRealm.where(Owner.class).notEqualTo("dogs.age", 10).findAll();
        assertEquals(1, owners2.size());

        RealmResults<Owner> all1 = testRealm.where(Owner.class).notEqualTo("dogs.age", 7).findAll();
        assertEquals(1, all1.size());

        RealmResults<Owner> owners3 = testRealm.where(Owner.class).greaterThan("dogs.age", 9).findAll();
        assertEquals(1, owners3.size());

        RealmResults<Owner> owners4 = testRealm.where(Owner.class).greaterThanOrEqualTo("dogs.age", 9).findAll();
        assertEquals(1, owners4.size());

        RealmResults<Owner> owners5 = testRealm.where(Owner.class).lessThan("dogs.age", 9).findAll();
        assertEquals(1, owners5.size());

        RealmResults<Owner> owners6 = testRealm.where(Owner.class).lessThanOrEqualTo("dogs.age", 9).findAll();
        assertEquals(1, owners6.size());

        RealmResults<Owner> owners7 = testRealm.where(Owner.class).between("dogs.age", 9, 11).findAll();
        assertEquals(1, owners7.size());
    }

    public void testQueryMultipleRelationsFloat() {
        RealmResults<Owner> owners1 = testRealm.where(Owner.class).greaterThanOrEqualTo("dogs.height", 0.2f).findAll();
        assertEquals(1, owners1.size());
        assertEquals(12, owners1.first().getCat().getAge());

        RealmResults<Owner> owners2 = testRealm.where(Owner.class).greaterThan("dogs.height", 0.2f).findAll();
        assertEquals(1, owners2.size());
        assertEquals(12, owners2.first().getCat().getAge());

        RealmResults<Owner> owners3 = testRealm.where(Owner.class).lessThan("dogs.height", 2.2f).findAll();
        assertEquals(1, owners3.size());
        assertEquals(12, owners3.first().getCat().getAge());

        RealmResults<Owner> owners4 = testRealm.where(Owner.class).lessThanOrEqualTo("dogs.height", 2.2f).findAll();
        assertEquals(1, owners4.size());
        assertEquals(12, owners4.first().getCat().getAge());

        RealmResults<Owner> owners5 = testRealm.where(Owner.class).notEqualTo("dogs.height", 0.2f).findAll();
        assertEquals(1, owners5.size());
        assertEquals(12, owners5.first().getCat().getAge());

        RealmResults<Owner> owners6 = testRealm.where(Owner.class).greaterThanOrEqualTo("dogs.height", 0.3f).findAll();
        assertEquals(1, owners6.size());
        assertEquals(12, owners6.first().getCat().getAge());

        RealmResults<Owner> owners7 = testRealm.where(Owner.class).between("dogs.height", 0.2f, 2.2f).findAll();
        assertEquals(1, owners7.size());
        assertEquals(12, owners7.first().getCat().getAge());
    }

    public void testQueryMultipleRelationsDouble() {
        RealmResults<Owner> owners1 = testRealm.where(Owner.class).greaterThanOrEqualTo("dogs.weight", 0.2).findAll();
        assertEquals(1, owners1.size());
        assertEquals(12, owners1.first().getCat().getAge());

        RealmResults<Owner> owners2 = testRealm.where(Owner.class).greaterThan("dogs.weight", 0.2).findAll();
        assertEquals(1, owners2.size());
        assertEquals(12, owners2.first().getCat().getAge());

        RealmResults<Owner> owners3 = testRealm.where(Owner.class).lessThan("dogs.weight", 12.2).findAll();
        assertEquals(1, owners3.size());
        assertEquals(12, owners3.first().getCat().getAge());

        RealmResults<Owner> owners4 = testRealm.where(Owner.class).lessThanOrEqualTo("dogs.weight", 12.2).findAll();
        assertEquals(1, owners4.size());
        assertEquals(12, owners4.first().getCat().getAge());

        RealmResults<Owner> owners5 = testRealm.where(Owner.class).notEqualTo("dogs.weight", 0.2).findAll();
        assertEquals(1, owners5.size());
        assertEquals(12, owners5.first().getCat().getAge());

        RealmResults<Owner> owners6 = testRealm.where(Owner.class).greaterThanOrEqualTo("dogs.weight", 0.3).findAll();
        assertEquals(1, owners6.size());
        assertEquals(12, owners6.first().getCat().getAge());

        RealmResults<Owner> owners7 = testRealm.where(Owner.class).between("dogs.weight", 0.2, 12.2).findAll();
        assertEquals(1, owners7.size());
        assertEquals(12, owners7.first().getCat().getAge());
    }


    public void testQueryMultipleRelationsString() {
        RealmResults<Owner> owners = testRealm.where(Owner.class).equalTo("dogs.name", "Pluto").findAll();
        assertEquals(1, owners.size());

        RealmResults<Owner> none = testRealm.where(Owner.class).equalTo("dogs.name", "King").findAll();
        assertEquals(0, none.size());
    }
}
