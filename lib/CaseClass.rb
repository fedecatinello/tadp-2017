class CaseClass

	def metodo_de_la_caseclass
		'hola'
	end

	def initialize
		super
	end

  def self.< (sym)
		return (self.ancestors.first.to_s + '~' + sym.to_s).to_s
	end

  def self.is_case_class?
		true
	end

end