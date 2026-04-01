    //a
    public Pet savePet(Pet pet, Long ownerId) {
        Customer owner = customerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + ownerId));
        pet.setOwner(owner);
        Pet saved = petRepository.save(pet);
        owner.getPets().add(saved);
        return saved;
    }

    public Pet getPet(Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found: " + petId));
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(Long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }

}
