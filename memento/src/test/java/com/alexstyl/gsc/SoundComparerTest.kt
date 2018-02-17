package com.alexstyl.gsc

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SoundComparerTest {

    @Test
    fun soundTheSame() {
        Assert.assertTrue(SoundComparer.soundTheSame("Αθήνα", "athina"))
        Assert.assertTrue(SoundComparer.soundTheSame("Αθήνα", "athina"))
        Assert.assertTrue(SoundComparer.soundTheSame("τζιτσαγκά", "jitsaga"))
        Assert.assertTrue(SoundComparer.soundTheSame("Αθήνα", "Athena"))
        Assert.assertTrue(SoundComparer.soundTheSame("Gilgames G", "Γκιλγγαμες Γ"))

        Assert.assertTrue(SoundComparer.soundTheSame("", ""))
        Assert.assertTrue(SoundComparer.soundTheSame("  ", "  "))
        Assert.assertFalse(SoundComparer.soundTheSame("Αθήνα", ""))
        Assert.assertFalse(SoundComparer.soundTheSame("", "AAAAA"))

        Assert.assertTrue(SoundComparer.soundTheSame("Yiannis", "Giannis"))
        Assert.assertTrue(SoundComparer.soundTheSame("ΕΛΕΟΣ", "ELEOC"))
        Assert.assertTrue(SoundComparer.soundTheSame("ΕΛΕΟΣ", "eLe0C"))
        Assert.assertTrue(SoundComparer.soundTheSame("apistefto", "απίστευτο"))
        Assert.assertTrue(SoundComparer.soundTheSame("sistima", "σύστημα"))
        Assert.assertTrue(SoundComparer.soundTheSame("einai", "είναι"))
        Assert.assertTrue(SoundComparer.soundTheSame("pragmatikotita", "πραγματικότητα"))

        Assert.assertTrue(SoundComparer.soundTheSame("ThaBo", "θάμπό"))
        Assert.assertTrue(SoundComparer.soundTheSame("Eytih0s", "Ευτηχώς"))
        Assert.assertTrue(SoundComparer.soundTheSame("Tha Pho Anthrwpoi PSOMY", "ΘΑ ΦΩ ΑΝΘΡΩΠΟΙ ΨΩΜΙ"))

        Assert.assertTrue(SoundComparer.soundTheSame("Ayto", "Άυτό"))
        Assert.assertTrue(SoundComparer.soundTheSame("Auto", "Αυτό"))
        Assert.assertTrue(SoundComparer.soundTheSame("Afto", "Αυτό"))
        Assert.assertTrue(SoundComparer.soundTheSame("Ayti", "Αφτί"))
        Assert.assertTrue(SoundComparer.soundTheSame("KSIFIAS", "XIPHIAS"))

        Assert.assertTrue(SoundComparer.soundTheSame("έχω", "exw"))
        Assert.assertTrue(SoundComparer.soundTheSame("έξω", "exw"))
        Assert.assertTrue(SoundComparer.soundTheSame("έχω", "exw"))
        Assert.assertTrue(SoundComparer.soundTheSame("έχω", "ehw"))
        Assert.assertTrue(SoundComparer.soundTheSame("έχω", "eho"))
        Assert.assertTrue(SoundComparer.soundTheSame("έχω", "exo"))
        Assert.assertTrue(SoundComparer.soundTheSame("exo", "eHo"))
        Assert.assertTrue(SoundComparer.soundTheSame("Έχω", "Εxo"))
        Assert.assertTrue(SoundComparer.soundTheSame("έχο", "eh0"))

        Assert.assertTrue(SoundComparer.soundTheSame("Αθήνα", "a8ina"))
        Assert.assertTrue(SoundComparer.soundTheSame("Αθήνα", "a9ina"))
        Assert.assertTrue(SoundComparer.soundTheSame("Αthήνα", "a9ina"))
        Assert.assertTrue(SoundComparer.soundTheSame("Γιώργος", "Giorgos"))
        Assert.assertTrue(SoundComparer.soundTheSame("Γιώργος", "giorgος"))
        Assert.assertTrue(SoundComparer.soundTheSame("Γιώργος", "Giwrgos"))
        Assert.assertTrue(SoundComparer.soundTheSame("Αλέξανδρος", "Aleksandros"))
        Assert.assertTrue(SoundComparer.soundTheSame("Αλέξανδρος", "Alexandros"))
        Assert.assertTrue(SoundComparer.soundTheSame("Αγγέλα", "Aggela"))
        Assert.assertTrue(SoundComparer.soundTheSame("Αγγέλα", "Agela"))
        Assert.assertTrue(SoundComparer.soundTheSame("Babis", "Μπάμπης"))
        Assert.assertTrue(SoundComparer.soundTheSame("Mpampis", "Μπάμπης"))

        Assert.assertTrue(SoundComparer.soundTheSame("athina", "Αθήνα"))
        Assert.assertTrue(SoundComparer.soundTheSame("Athina", "Αθήνα"))
        Assert.assertTrue(SoundComparer.soundTheSame("a8ina", "Αθήνα"))
        Assert.assertTrue(SoundComparer.soundTheSame("Αθήνα", "Αθηνα"))
        Assert.assertTrue(SoundComparer.soundTheSame("πτεροδάκτυλος", "pterodaktilos"))
        Assert.assertTrue(SoundComparer.soundTheSame("ΣΚΟΥΛΙΚΟΜΙΡΜΙΓΚΟΤΡΥΠΑΩ", "skoylikomirmiggotripaw"))
        Assert.assertTrue(SoundComparer.soundTheSame("ΣΚΟΥΛΙΚΟΜΙΡΜΙΓΚΟΤΡΥΠΑΩ", "skoylikomirmigotripaw"))


        Assert.assertFalse(SoundComparer.soundTheSame("έξω", "έχω"))
        Assert.assertFalse(SoundComparer.soundTheSame("Υτα", "FTA"))
        Assert.assertFalse(SoundComparer.soundTheSame("Αθήνα", "patra"))
        Assert.assertFalse(SoundComparer.soundTheSame("Αθήν", "athina"))
        Assert.assertFalse(SoundComparer.soundTheSame("Αθήνα", "athin"))
        Assert.assertFalse(SoundComparer.soundTheSame("Αθή", "athina"))
        Assert.assertFalse(SoundComparer.soundTheSame("Αθή", "ath"))
        Assert.assertFalse(SoundComparer.soundTheSame("Αθή", "ath"))
        Assert.assertFalse(SoundComparer.soundTheSame("Αθή", "ath"))
        Assert.assertFalse(SoundComparer.soundTheSame("Αθή", "ath"))
        Assert.assertFalse(SoundComparer.soundTheSame("Αθήα", "athie"))
        Assert.assertFalse(SoundComparer.soundTheSame("KSIFIA", "XIPHI"))
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
