package com.example.aston_recyclerview_hw1

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.aston_recyclerview_hw1.utils.Contact
import com.github.javafaker.Faker


open class MainActivity : AppCompatActivity(),
    DetailsFragment.OnSaveChangesListener {

    private lateinit var fragmentContacts: Fragment
    private lateinit var fragmentDetails: Fragment
    private lateinit var fTrans: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            fillDataList()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_view, ContactsFragment::class.java, null)
                .commit()
        }

        fragmentContacts = ContactsFragment()

        fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fragment_container_view, fragmentContacts, "ContactsFragment")
        fTrans.commit()

    }

    private fun fillDataList() {
        val faker = Faker()
        for (i in 8..110) {
            val firstName = faker.name().firstName()
            val lastName = faker.name().lastName()
            val phone = faker.phoneNumber().cellPhone()
            val additionalInfo = faker.address().streetAddress()
            var imageUrl = "https://picsum.photos/id/${i + 20}/70/70"
            if (i == 66 || i == 77 || i == 85) imageUrl =
                "https://picsum.photos/id/${i + 25}/100/100"
            contactsList.add(Contact(i, firstName, lastName, phone, additionalInfo, imageUrl))
        }
    }

    override fun saveChanges(number: Int, details: Contact) {
        val detailsFrame: View? = findViewById(R.id.fragment_container_view2)
        val contactFragment: ContactsFragment? =
            supportFragmentManager.findFragmentByTag("ContactsFragment") as ContactsFragment?
        val oldList = contactsList.map { it.copy() }

        var needToChange = 0
        for (i in 0 until contactsList.size) {
            if (details.id == contactsList[i].id) needToChange = i
        }

        contactsList[needToChange] = details

        contactFragment?.updateContactToRecyclerView(oldList)
        contactFragment?.filter(contactFragment.textFromSearch)
        val dualMode = (detailsFrame != null
                && detailsFrame.visibility == View.VISIBLE)
        if (dualMode) {
            fragmentDetails = DetailsFragment.newInstance(number, details.toStringArray())
            fTrans = supportFragmentManager.beginTransaction()
            fTrans.replace(R.id.fragment_container_view2, fragmentDetails, "DetailsFragment")
            fTrans.commit()
        } else {
            onBackPressed()
        }
    }


    companion object {
        var contactsList = mutableListOf(
            Contact(
                0,
                "Liara",
                "T'Soni",
                "89211105040",
                "Archaeologist and protean specialist",
                "https://masseffect-universe.com/_ph/31/677892442.jpg"
            ),
            Contact(
                1,
                "Commander",
                "Shepherd",
                "89213334151",
                "Saviour of the galaxy",
                "https://ixbt.online/gametech/covers/2021/05/15/D1Ot4Z4V83OAcH5gVZ4QOoTI3Tgt3oFB3Kd63NYq.jpg"
            ),
            Contact(
                2,
                "Garrus",
                "Vakarian",
                "89215325431",
                "Rogue C-Sec operative",
                "https://i.pinimg.com/originals/c2/9c/58/c29c58eaff29b0301b0e671b06670e2c.png"
            ),
            Contact(
                3,
                "Tali'Zorah",
                "nar Rayya",
                "89601214536",
                "Gifted engineer",
                "https://sleety.org/files/2010/03/masseffect2-2010-03-16-18-38-03-34.jpg"
            ),
            Contact(
                4,
                "Kaidan",
                "Alenko",
                "89316627623",
                " Systems Alliance Marine",
                "https://masseffect-universe.com/_ph/31/327883582.jpg"
            ),
            Contact(
                5,
                "Ashley",
                "Williams",
                "89992317694",
                " Systems Alliance Marine",
                "https://img3.goodfon.ru/wallpaper/nbig/5/5c/mass-effect-n7-ashley-williams.jpg"
            ),
            Contact(
                6,
                "Rex",
                "Urdnot",
                "89416388285",
                " Krogan mercenary and bounty hunter",
                "https://i.pinimg.com/736x/44/d9/90/44d99085e64ddab6369b0c6e285e39f2--mass-effect--crumpets.jpg"
            ),
            Contact(
                7,
                "Karin",
                "Chakwas",
                "89994127594",
                "Flight medic",
                "https://i.pinimg.com/736x/f3/50/c1/f350c19627e3d5e16259bc865e8d003c--mass-effect--videogames.jpg"
            ),
        )

    }
}