# Conge


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**createdAt** | **string** |  | [optional] [default to undefined]
**createdBy** | **string** |  | [optional] [default to undefined]
**updatedAt** | **string** |  | [optional] [default to undefined]
**updatedBy** | **string** |  | [optional] [default to undefined]
**id** | **number** |  | [optional] [default to undefined]
**dateDepart** | **string** |  | [optional] [default to undefined]
**getdDepart** | **string** |  | [optional] [default to undefined]
**dateRetour** | **string** |  | [optional] [default to undefined]
**getdRetour** | **string** |  | [optional] [default to undefined]
**nombrePart** | **number** |  | [optional] [default to undefined]
**nbrePart** | **string** |  | [optional] [default to undefined]
**tempsPresenceEffectif** | **number** |  | [optional] [default to undefined]
**salaireMoyenMensuel** | **number** |  | [optional] [default to undefined]
**moisOfpresence** | **number** |  | [optional] [default to undefined]
**tempsOfpresence** | **number** |  | [optional] [default to undefined]
**montantSalaireMoyenMensuel** | **string** |  | [optional] [default to undefined]
**indemniteRepresentationMoyenMensuelle** | **number** |  | [optional] [default to undefined]
**indemniteRepresentMoyenMensuel** | **string** |  | [optional] [default to undefined]
**nombreJourCongeDu** | **number** |  | [optional] [default to undefined]
**nombreJourCongePris** | **number** |  | [optional] [default to undefined]
**provisionConge** | **number** |  | [optional] [default to undefined]
**montantProvisionConge** | **string** |  | [optional] [default to undefined]
**jouradditionel** | **string** |  | [optional] [default to undefined]
**baseImposableAllocationConge** | **number** |  | [optional] [default to undefined]
**montantBaseImposableAllocationConge** | **string** |  | [optional] [default to undefined]
**impotSalaire** | **number** |  | [optional] [default to undefined]
**montantIs** | **string** |  | [optional] [default to undefined]
**ta** | **number** |  | [optional] [default to undefined]
**montantTa** | **string** |  | [optional] [default to undefined]
**fpc** | **number** |  | [optional] [default to undefined]
**montantFpc** | **string** |  | [optional] [default to undefined]
**totalProvisionImpot** | **number** |  | [optional] [default to undefined]
**montantTotalProvisionImpot** | **string** |  | [optional] [default to undefined]
**prestationFamiliale** | **number** |  | [optional] [default to undefined]
**prestationFamil** | **string** |  | [optional] [default to undefined]
**accidentTravail** | **number** |  | [optional] [default to undefined]
**accidentTrav** | **string** |  | [optional] [default to undefined]
**retraite** | **number** |  | [optional] [default to undefined]
**montantRetraite** | **string** |  | [optional] [default to undefined]
**totalProvisionChargeSocial** | **number** |  | [optional] [default to undefined]
**montantTotalProvisionChargeSocial** | **string** |  | [optional] [default to undefined]
**totalChargePatronale** | **number** |  | [optional] [default to undefined]
**montantTotalChargePatronale** | **string** |  | [optional] [default to undefined]
**its** | **number** |  | [optional] [default to undefined]
**montantITS** | **string** |  | [optional] [default to undefined]
**cn** | **number** |  | [optional] [default to undefined]
**montantCN** | **string** |  | [optional] [default to undefined]
**igr** | **number** |  | [optional] [default to undefined]
**montantIGR** | **string** |  | [optional] [default to undefined]
**tpsdepresence** | **string** |  | [optional] [default to undefined]
**nbcongedu** | **string** |  | [optional] [default to undefined]
**cnps** | **number** |  | [optional] [default to undefined]
**montantCNPS** | **string** |  | [optional] [default to undefined]
**totalRetenue** | **number** |  | [optional] [default to undefined]
**montantTotalRetenue** | **string** |  | [optional] [default to undefined]
**allocationCongeNet** | **number** |  | [optional] [default to undefined]
**montantAllocationCongeNet** | **string** |  | [optional] [default to undefined]
**allocationCongeNetPris** | **number** |  | [optional] [default to undefined]
**montantAllocationCongeNetPris** | **string** |  | [optional] [default to undefined]
**listImprimBulletinPaie** | [**Array&lt;ImprimBulletinPaie&gt;**](ImprimBulletinPaie.md) |  | [optional] [default to undefined]
**listImprimBulletinPaieEngagement** | [**Array&lt;ImprimBulletinPaie&gt;**](ImprimBulletinPaie.md) |  | [optional] [default to undefined]
**listImprimBulletinPaieIndemniteNonImp** | [**Array&lt;ImprimBulletinPaie&gt;**](ImprimBulletinPaie.md) |  | [optional] [default to undefined]
**montantcumulIts** | **string** |  | [optional] [default to undefined]
**montantNetPayer** | **string** |  | [optional] [default to undefined]
**montantcumulCn** | **string** |  | [optional] [default to undefined]
**montantcumulIgr** | **string** |  | [optional] [default to undefined]
**montantcumulCnpsSal** | **string** |  | [optional] [default to undefined]
**alocnetpayer** | **string** |  | [optional] [default to undefined]
**reeljrdu** | **number** |  | [optional] [default to undefined]
**contratPersonnel** | [**ContratPersonnel**](ContratPersonnel.md) |  | [optional] [default to undefined]
**periodePaie** | [**PeriodePaie**](PeriodePaie.md) |  | [optional] [default to undefined]

## Example

```typescript
import { Conge } from './api';

const instance: Conge = {
    createdAt,
    createdBy,
    updatedAt,
    updatedBy,
    id,
    dateDepart,
    getdDepart,
    dateRetour,
    getdRetour,
    nombrePart,
    nbrePart,
    tempsPresenceEffectif,
    salaireMoyenMensuel,
    moisOfpresence,
    tempsOfpresence,
    montantSalaireMoyenMensuel,
    indemniteRepresentationMoyenMensuelle,
    indemniteRepresentMoyenMensuel,
    nombreJourCongeDu,
    nombreJourCongePris,
    provisionConge,
    montantProvisionConge,
    jouradditionel,
    baseImposableAllocationConge,
    montantBaseImposableAllocationConge,
    impotSalaire,
    montantIs,
    ta,
    montantTa,
    fpc,
    montantFpc,
    totalProvisionImpot,
    montantTotalProvisionImpot,
    prestationFamiliale,
    prestationFamil,
    accidentTravail,
    accidentTrav,
    retraite,
    montantRetraite,
    totalProvisionChargeSocial,
    montantTotalProvisionChargeSocial,
    totalChargePatronale,
    montantTotalChargePatronale,
    its,
    montantITS,
    cn,
    montantCN,
    igr,
    montantIGR,
    tpsdepresence,
    nbcongedu,
    cnps,
    montantCNPS,
    totalRetenue,
    montantTotalRetenue,
    allocationCongeNet,
    montantAllocationCongeNet,
    allocationCongeNetPris,
    montantAllocationCongeNetPris,
    listImprimBulletinPaie,
    listImprimBulletinPaieEngagement,
    listImprimBulletinPaieIndemniteNonImp,
    montantcumulIts,
    montantNetPayer,
    montantcumulCn,
    montantcumulIgr,
    montantcumulCnpsSal,
    alocnetpayer,
    reeljrdu,
    contratPersonnel,
    periodePaie,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
