package com.alexstyl.gsc

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SoundComparerTest {


    @Test
    fun areSame() {
        Assert.assertTrue(SoundComparer.areSame("Αθήνα", "athina"))
        Assert.assertTrue(SoundComparer.areSame("Αθήνα", "athina"))
        Assert.assertTrue(SoundComparer.areSame("τζιτσαγκά", "jitsaga"))
        Assert.assertTrue(SoundComparer.areSame("Αθήνα", "Athena"))
        Assert.assertTrue(SoundComparer.areSame("Gilgames G", "Γκιλγγαμες Γ"))

        Assert.assertTrue(SoundComparer.areSame("", ""))
        Assert.assertTrue(SoundComparer.areSame("  ", "  "))
        Assert.assertFalse(SoundComparer.areSame("Αθήνα", ""))
        Assert.assertFalse(SoundComparer.areSame("", "AAAAA"))

        Assert.assertTrue(SoundComparer.areSame("Yiannis", "Giannis"))
        Assert.assertTrue(SoundComparer.areSame("ΕΛΕΟΣ", "ELEOC"))
        Assert.assertTrue(SoundComparer.areSame("ΕΛΕΟΣ", "eLe0C"))
        Assert.assertTrue(SoundComparer.areSame("apistefto", "απίστευτο"))
        Assert.assertTrue(SoundComparer.areSame("sistima", "σύστημα"))
        Assert.assertTrue(SoundComparer.areSame("einai", "είναι"))
        Assert.assertTrue(SoundComparer.areSame("pragmatikotita", "πραγματικότητα"))

        Assert.assertTrue(SoundComparer.areSame("ThaBo", "θάμπό"))
        Assert.assertTrue(SoundComparer.areSame("Eytih0s", "Ευτηχώς"))
        Assert.assertTrue(SoundComparer.areSame("Tha Pho Anthrwpoi PSOMY", "ΘΑ ΦΩ ΑΝΘΡΩΠΟΙ ΨΩΜΙ"))

        Assert.assertTrue(SoundComparer.areSame("Ayto", "Άυτό"))
        Assert.assertTrue(SoundComparer.areSame("Auto", "Αυτό"))
        Assert.assertTrue(SoundComparer.areSame("Afto", "Αυτό"))
        Assert.assertTrue(SoundComparer.areSame("Ayti", "Αφτί"))
        Assert.assertTrue(SoundComparer.areSame("KSIFIAS", "XIPHIAS"))

        Assert.assertTrue(SoundComparer.areSame("έχω", "exw"))
        Assert.assertTrue(SoundComparer.areSame("έξω", "exw"))
        Assert.assertTrue(SoundComparer.areSame("έχω", "exw"))
        Assert.assertTrue(SoundComparer.areSame("έχω", "ehw"))
        Assert.assertTrue(SoundComparer.areSame("έχω", "eho"))
        Assert.assertTrue(SoundComparer.areSame("έχω", "exo"))
        Assert.assertTrue(SoundComparer.areSame("exo", "eHo"))
        Assert.assertTrue(SoundComparer.areSame("Έχω", "Εxo"))
        Assert.assertTrue(SoundComparer.areSame("έχο", "eh0"))

        Assert.assertTrue(SoundComparer.areSame("Αθήνα", "a8ina"))
        Assert.assertTrue(SoundComparer.areSame("Αθήνα", "a9ina"))
        Assert.assertTrue(SoundComparer.areSame("Αthήνα", "a9ina"))
        Assert.assertTrue(SoundComparer.areSame("Γιώργος", "Giorgos"))
        Assert.assertTrue(SoundComparer.areSame("Γιώργος", "giorgος"))
        Assert.assertTrue(SoundComparer.areSame("Γιώργος", "Giwrgos"))
        Assert.assertTrue(SoundComparer.areSame("Αλέξανδρος", "Aleksandros"))
        Assert.assertTrue(SoundComparer.areSame("Αλέξανδρος", "Alexandros"))
        Assert.assertTrue(SoundComparer.areSame("Αγγέλα", "Aggela"))
        Assert.assertTrue(SoundComparer.areSame("Αγγέλα", "Agela"))
        Assert.assertTrue(SoundComparer.areSame("Babis", "Μπάμπης"))
        Assert.assertTrue(SoundComparer.areSame("Mpampis", "Μπάμπης"))

        Assert.assertTrue(SoundComparer.areSame("athina", "Αθήνα"))
        Assert.assertTrue(SoundComparer.areSame("Athina", "Αθήνα"))
        Assert.assertTrue(SoundComparer.areSame("a8ina", "Αθήνα"))
        Assert.assertTrue(SoundComparer.areSame("Αθήνα", "Αθηνα"))
        Assert.assertTrue(SoundComparer.areSame("πτεροδάκτυλος", "pterodaktilos"))
        Assert.assertTrue(SoundComparer.areSame("ΣΚΟΥΛΙΚΟΜΙΡΜΙΓΚΟΤΡΥΠΑΩ", "skoylikomirmiggotripaw"))
        Assert.assertTrue(SoundComparer.areSame("ΣΚΟΥΛΙΚΟΜΙΡΜΙΓΚΟΤΡΥΠΑΩ", "skoylikomirmigotripaw"))


        Assert.assertFalse(SoundComparer.areSame("έξω", "έχω"))
        Assert.assertFalse(SoundComparer.areSame("Υτα", "FTA"))
        Assert.assertFalse(SoundComparer.areSame("Αθήνα", "patra"))
        Assert.assertFalse(SoundComparer.areSame("Αθήν", "athina"))
        Assert.assertFalse(SoundComparer.areSame("Αθήνα", "athin"))
        Assert.assertFalse(SoundComparer.areSame("Αθή", "athina"))
        Assert.assertFalse(SoundComparer.areSame("Αθή", "ath"))
        Assert.assertFalse(SoundComparer.areSame("Αθή", "ath"))
        Assert.assertFalse(SoundComparer.areSame("Αθή", "ath"))
        Assert.assertFalse(SoundComparer.areSame("Αθή", "ath"))
        Assert.assertFalse(SoundComparer.areSame("Αθήα", "athie"))
        Assert.assertFalse(SoundComparer.areSame("KSIFIA", "XIPHI"))
    }


    @Test
    fun name() {
        Assert.assertTrue(SoundComparer.startsWith("Babis", "Μπάμπ"))
    }

    @Test
    fun startsWith() {
        Assert.assertFalse(SoundComparer.startsWith("Αθήνα", "patra"))
        Assert.assertFalse(SoundComparer.startsWith("Αθήναx", "Αθήναs"))
        Assert.assertFalse(SoundComparer.startsWith("Αθήνεο", "Αθήνει"))
        Assert.assertFalse(SoundComparer.startsWith("Αθήνα", "ak"))

        Assert.assertTrue(SoundComparer.startsWith("Apopse", "Αποψ"))
        Assert.assertTrue(SoundComparer.startsWith("Αποψε", "Apop"))
        Assert.assertFalse(SoundComparer.startsWith("Αποψ", "Apopse"))
        Assert.assertFalse(SoundComparer.startsWith("Αποψε", "Apope"))

        Assert.assertTrue(SoundComparer.startsWith("Babis", "Μπάμπ"))
        Assert.assertTrue(SoundComparer.startsWith("Babis", "Μπάμ"))
        Assert.assertTrue(SoundComparer.startsWith("Babis", "Μ"))
        Assert.assertTrue(SoundComparer.startsWith("Αθήνα", "athin"))
        Assert.assertTrue(SoundComparer.startsWith("Αθήνα", "ath"))
        Assert.assertTrue(SoundComparer.startsWith("Αθήνα", "at"))

        Assert.assertTrue(SoundComparer.startsWith("Athena", "Αθήνα"))
        Assert.assertTrue(SoundComparer.startsWith("Athena", "Αθήν"))
        Assert.assertTrue(SoundComparer.startsWith("Athena", "Αθή"))
        Assert.assertTrue(SoundComparer.startsWith("Athena", "Αθ"))
        Assert.assertTrue(SoundComparer.startsWith("Athena", "Α"))

        Assert.assertTrue(SoundComparer.startsWith("Anthropoi", "Άνθρωποι"))
        Assert.assertTrue(SoundComparer.startsWith("Anthropoi", "Άνθρωπο"))
        Assert.assertTrue(SoundComparer.startsWith("Anthropoi", "Άνθρωπ"))
        Assert.assertTrue(SoundComparer.startsWith("Anthropoi", "Άνθρω"))

        Assert.assertFalse(SoundComparer.startsWith("Μπά", "Babis"))

        Assert.assertTrue(SoundComparer.startsWith("ΚΣ", "Κ"))
        Assert.assertTrue(SoundComparer.startsWith("ΟΞιφίας", "OK"))
        Assert.assertTrue(SoundComparer.startsWith("ΟΞιφίας", "ΟKSIP"))
        Assert.assertTrue(SoundComparer.startsWith("ΟΞιφίας", "ΟKSIPH"))
        Assert.assertTrue(SoundComparer.startsWith("ΟΞιφίας", "ΟKSIPHIAS"))
    }
}
