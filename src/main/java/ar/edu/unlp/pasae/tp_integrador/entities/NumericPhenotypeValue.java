package ar.edu.unlp.pasae.tp_integrador.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class NumericPhenotypeValue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @OneToOne
    private NumericPhenotype phenotype;
    private Long value;

    public NumericPhenotypeValue(NumericPhenotype phenotype, Long value) {
        super();
        this.setPhenotype(phenotype);
        this.setValue(value);
    }

	/**
	 * @return the value
	 */
	public Long getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Long value) {
		this.value = value;
	}

	/**
	 * @return the phenotype
	 */
	public NumericPhenotype getPhenotype() {
		return phenotype;
	}

	/**
	 * @param phenotype the phenotype to set
	 */
	public void setPhenotype(NumericPhenotype phenotype) {
		this.phenotype = phenotype;
	}
}
