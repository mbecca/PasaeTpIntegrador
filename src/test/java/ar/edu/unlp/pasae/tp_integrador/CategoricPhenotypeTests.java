package ar.edu.unlp.pasae.tp_integrador;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import ar.edu.unlp.pasae.tp_integrador.dtos.CategoricPhenotypeDTO;
import ar.edu.unlp.pasae.tp_integrador.entities.CategoricPhenotype;
import ar.edu.unlp.pasae.tp_integrador.services.CategoricPhenotypeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class CategoricPhenotypeTests {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private CategoricPhenotypeService phenotypeService;

	@Test
	public void it_returns_phenotype_list() {
		Collection<CategoricPhenotypeDTO> phenotypes;

		phenotypes = this.phenotypeService.list().collect(Collectors.toList());

		Assert.assertTrue("Phenotype list should be empty", phenotypes.isEmpty());

		for (Long i = 1L; i <= 10; i++) {
			CategoricPhenotypeDTO request;
			Map<Long, String> values = new HashMap<Long, String>();
			values.put(i, "Value " + i);

			request = new CategoricPhenotypeDTO("Phenotype #" + i, values);
			this.phenotypeService.create(request);
		}

		phenotypes = this.phenotypeService.list().collect(Collectors.toList());

		Assert.assertFalse("Phenotype list should not be empty", phenotypes.isEmpty());
		Assert.assertEquals(10, phenotypes.size());
	}

	@Test
	public void it_returns_phenotype_count() {
		Assert.assertEquals((Integer) 0, this.phenotypeService.count());

		for (Long i = 1L; i <= 10; i++) {
			CategoricPhenotypeDTO request;
			Map<Long, String> values = new HashMap<Long, String>();
			values.put(i, "Value " + i);

			request = new CategoricPhenotypeDTO("Phenotype #" + i, values);
			this.phenotypeService.create(request);
		}

		Assert.assertEquals((Integer) 10, this.phenotypeService.count());
	}

	@Test
	public void it_finds_phenotype_by_id() {
		String name = "Name";
		Map<Long, String> values = new HashMap<Long, String>();
		values.put(1L, "Value 1");
		values.put(2L, "Value 2");
		values.put(3L, "Value 3");

		CategoricPhenotypeDTO request = new CategoricPhenotypeDTO(name, values);
		Long phenotypeId = this.phenotypeService.create(request).getId();
		CategoricPhenotypeDTO phenotype = this.phenotypeService.find(phenotypeId);

		Assert.assertNotNull(phenotype);
		Assert.assertEquals(name, phenotype.getName());
		Assert.assertEquals(values.size(), phenotype.getValues().size());
		Assert.assertTrue(phenotype.getValues().keySet().containsAll(values.keySet()));
		Assert.assertTrue(phenotype.getValues().values().containsAll(values.values()));
	}

	@Test
	public void it_throws_exception_when_phenotype_id_does_not_exist() {
		thrown.expect(EntityNotFoundException.class);

		this.phenotypeService.find(1L);
		this.phenotypeService.find(2L);
	}

	@Test
	public void it_finds_phenotype_by_name() {
		String name = "Name";
		Map<Long, String> values = new HashMap<Long, String>();
		values.put(1L, "Value 1");
		values.put(2L, "Value 2");
		values.put(3L, "Value 3");

		CategoricPhenotypeDTO request = new CategoricPhenotypeDTO(name, values);
		this.phenotypeService.create(request).getId();
		CategoricPhenotypeDTO phenotype = this.phenotypeService.findByName(name);

		Assert.assertNotNull(phenotype);
		Assert.assertEquals(name, phenotype.getName());
		Assert.assertEquals(values.size(), phenotype.getValues().size());
		Assert.assertTrue(phenotype.getValues().keySet().containsAll(values.keySet()));
		Assert.assertTrue(phenotype.getValues().values().containsAll(values.values()));
	}

	@Test
	public void it_throws_exception_when_phenotype_name_does_not_exist() {
		thrown.expect(EntityNotFoundException.class);

		this.phenotypeService.findByName("");
		this.phenotypeService.findByName("123");
		this.phenotypeService.findByName("12345678");
	}

	@Test
	public void it_deletes_phenotype() {
		Map<Long, String> values = new HashMap<Long, String>();
		values.put(1L, "Value 1");

		CategoricPhenotypeDTO request = new CategoricPhenotypeDTO("Name", values);
		Long phenotypeId = this.phenotypeService.create(request).getId();

		CategoricPhenotypeDTO phenotype;

		phenotype = this.phenotypeService.find(phenotypeId);
		Assert.assertNotNull(phenotype);

		thrown.expect(EntityNotFoundException.class);

		this.phenotypeService.delete(phenotypeId);
		phenotype = this.phenotypeService.find(phenotypeId);
		Assert.assertNull(phenotype);
	}

	@Test
	public void it_throws_exception_when_deleting_non_existing_phenotype() {
		thrown.expect(EntityNotFoundException.class);

		this.phenotypeService.delete(1L);
		this.phenotypeService.delete(2L);
	}

	@Test
	public void it_saves_new_phenotype() {
		String name = "Name";
		Map<Long, String> values = new HashMap<Long, String>();
		values.put(1L, "Value 1");
		values.put(2L, "Value 2");
		values.put(3L, "Value 3");

		CategoricPhenotypeDTO request = new CategoricPhenotypeDTO(name, values);
		CategoricPhenotypeDTO phenotype = this.phenotypeService.create(request);

		Assert.assertNotNull(phenotype);
		Assert.assertEquals(name, phenotype.getName());
		Assert.assertEquals(values.size(), phenotype.getValues().size());
		Assert.assertTrue(phenotype.getValues().keySet().containsAll(values.keySet()));
		Assert.assertTrue(phenotype.getValues().values().containsAll(values.values()));
	}

	@Test
	public void it_updates_existing_phenotype() {
		Map<Long, String> values = new HashMap<Long, String>();
		values.put(1L, "Value 1");
		values.put(2L, "Value 2");
		values.put(3L, "Value 3");

		CategoricPhenotypeDTO createRequest = new CategoricPhenotypeDTO("Name", values);
		CategoricPhenotypeDTO phenotype = this.phenotypeService.create(createRequest);

		String name = "Changed Name";
		Map<Long, String> updatedValues = new HashMap<Long, String>();
		updatedValues.put(4L, "Value 4");
		updatedValues.put(5L, "Value 5");

		CategoricPhenotypeDTO updateRequest = new CategoricPhenotypeDTO(name, updatedValues);
		CategoricPhenotypeDTO updatedPhenotype = this.phenotypeService.update(phenotype.getId(), updateRequest);

		Assert.assertEquals(name, updatedPhenotype.getName());
		Assert.assertEquals(updatedValues.size(), updatedPhenotype.getValues().size());
		Assert.assertTrue(updatedPhenotype.getValues().keySet().containsAll(updatedValues.keySet()));
		Assert.assertTrue(updatedPhenotype.getValues().values().containsAll(updatedValues.values()));
	}

	@Test
	public void it_does_not_allow_empty_name() {
		String name = "";
		Map<Long, String> values = new HashMap<Long, String>();
		values.put(1L, "Value 1");
		values.put(2L, "Value 2");
		values.put(3L, "Value 3");

		CategoricPhenotypeDTO request = new CategoricPhenotypeDTO(name, values);

		thrown.expect(ValidationException.class);
		this.phenotypeService.create(request);
	}

	@Test
	public void it_does_not_allow_empty_values() {
		String name = "Name";
		Map<Long, String> values = new HashMap<Long, String>();

		CategoricPhenotypeDTO request = new CategoricPhenotypeDTO(name, values);

		thrown.expect(ValidationException.class);
		this.phenotypeService.create(request);
	}

	@Test
	public void it_returns_true_for_valid_values() {
		String name = "Name";
		Map<Long, String> values = new HashMap<Long, String>();
		values.put(1L, "Value 1");
		values.put(2L, "Value 2");
		values.put(3L, "Value 3");

		CategoricPhenotype phenotype = new CategoricPhenotype(name, values);
		Assert.assertTrue(phenotype.validate("Value 1"));
		Assert.assertTrue(phenotype.validate("Value 2"));
		Assert.assertTrue(phenotype.validate("Value 3"));
	}

	@Test
	public void it_returns_false_for_invalid_values() {
		String name = "Name";
		Map<Long, String> values = new HashMap<Long, String>();
		values.put(1L, "Value 1");
		values.put(2L, "Value 2");
		values.put(3L, "Value 3");

		CategoricPhenotype phenotype = new CategoricPhenotype(name, values);
		Assert.assertFalse(phenotype.validate(""));
		Assert.assertFalse(phenotype.validate("aaa"));
		Assert.assertFalse(phenotype.validate("Value1"));
	}
}