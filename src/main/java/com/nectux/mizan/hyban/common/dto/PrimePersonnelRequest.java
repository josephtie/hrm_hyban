package com.nectux.mizan.hyban.common.dto;

public class PrimePersonnelRequest extends PaginationRequest {
    private Long id;
    private Double montantop;
    private Long valeurop;
    private Long idAbsence;
    private Long idPersonnel;
    private Long idCtrat;
    private Long ctrat;
    private Long idPrime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getMontantop() { return montantop; }
    public void setMontantop(Double montantop) { this.montantop = montantop; }
    public Long getValeurop() { return valeurop; }
    public void setValeurop(Long valeurop) { this.valeurop = valeurop; }
    public Long getIdAbsence() { return idAbsence; }
    public void setIdAbsence(Long idAbsence) { this.idAbsence = idAbsence; }
    public Long getIdPersonnel() { return idPersonnel; }
    public void setIdPersonnel(Long idPersonnel) { this.idPersonnel = idPersonnel; }
    public Long getIdCtrat() { return idCtrat; }
    public void setIdCtrat(Long idCtrat) { this.idCtrat = idCtrat; }
    public Long getCtrat() { return ctrat; }
    public void setCtrat(Long ctrat) { this.ctrat = ctrat; }
    public Long getIdPrime() { return idPrime; }
    public void setIdPrime(Long idPrime) { this.idPrime = idPrime; }
}
