package com.nectux.mizan.hyban.paie.dto;

import com.nectux.mizan.hyban.paie.entity.BulletinPaie;
import com.nectux.mizan.hyban.paie.entity.ImprimBulletinPaie;

import java.util.List;

public class RapportBullDTO {

    private BulletinPaie bulletinPaie;
    private List<ImprimBulletinPaie>imprimBulletinPaies;

    public BulletinPaie getBulletinPaie() {
        return bulletinPaie;
    }

    public void setBulletinPaie(BulletinPaie bulletinPaie) {
        this.bulletinPaie = bulletinPaie;
    }

    public List<ImprimBulletinPaie> getImprimBulletinPaies() {
        return imprimBulletinPaies;
    }

    public void setImprimBulletinPaies(List<ImprimBulletinPaie> imprimBulletinPaies) {
        this.imprimBulletinPaies = imprimBulletinPaies;
    }
}
